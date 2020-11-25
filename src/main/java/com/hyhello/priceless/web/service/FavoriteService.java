package com.hyhello.priceless.web.service;

import com.google.common.collect.Lists;
import com.hyhello.priceless.fileconfig.TokenConfig;
import com.hyhello.priceless.dataaccess.entity.FavoriteRecord;
import com.hyhello.priceless.dataaccess.repository.FavoriteRecordRepository;
import com.hyhello.priceless.module.cos.COSBucketSupport;
import com.hyhello.priceless.module.cos.COSService;
import com.hyhello.priceless.module.youget.YouGetService;
import com.hyhello.priceless.support.BeanSupport;
import com.hyhello.priceless.utils.ByteUtils;
import com.hyhello.priceless.utils.UrlUtils;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class FavoriteService {

    private final YouGetService youGetService;

    private final COSService cosService;

    private final ThreadPoolExecutor favoriteExecutor;

    private final TokenConfig tokenConfig;

    private final FavoriteRecordRepository favoriteRecordRepository;

    @Autowired
    public FavoriteService(YouGetService youGetService, COSService cosService, @Qualifier("FavoritePool") ThreadPoolExecutor executor, @Qualifier("tokenConfig") TokenConfig tokenConfig, FavoriteRecordRepository favoriteRecordRepository) {
        this.youGetService = youGetService;
        this.cosService = cosService;
        this.favoriteExecutor = executor;
        this.tokenConfig = tokenConfig;
        this.favoriteRecordRepository = favoriteRecordRepository;
    }

    public void addFavorite(String url, List<String> target){
        addFavorite(url, target, BeanSupport.getCommonConfig().getYougetTempDir(), "");
    }

    public void addFavorite(String url, List<String> target, String localPath, String comment ){

        if (ObjectUtils.isEmpty(target)){
            log.warn("no target specify while add favorite");
            target = Lists.newArrayList("local");
        }

        final List<String> t = target;

        Runnable favoriteTask = () -> {
            Future<File> future = youGetService.doTask(url, localPath);
            File file = null;

            try {
                if (future != null){
                    file = future.get(15, TimeUnit.MINUTES);
                }

                UploadResult result = null;
                if (file != null && file.exists()){
                    if (t.contains("cos")){
                        //cosService Object Oriented
                        log.info("upload cos start: " + url);
                        Upload upload = cosService.uploadFavorite(file);
                        try {
                            upload.waitForCompletion(); //阻塞
                            result = upload.waitForUploadResult();
                            log.info("upload cos complete: " + url);

                        } catch (CosClientException e) {
                            log.error("upload cos failed", e);
                        }
                    }

                    insertDB(url, file, result, comment);
                    if (!t.contains("local")){
                        String ok = file.delete() ? "成功" : "失败";
                        log.info("删除本地文件" + file.getName() + ":" + ok);
                    }
                }

                //TODO SELECT QUALITY, PLUGIN, DISTINCT
                //TODO COS LOG ?? - EVENT_DISPATCHER
                //TODO COS CLIENT IDLE ?? - Queue
                // WEBSOCKET KEEP COMMUNICATE

            } catch (InterruptedException e) {
                log.warn("InterruptedException in addFavorite", e);
                Thread.currentThread().interrupt();
            } catch (Exception e1){
                log.error("Exception in favoriteTask" , e1);
            }
        };

        favoriteExecutor.submit(favoriteTask);
    }

    private void insertDB(String url, File file, UploadResult result, String comment){

        FavoriteRecord favoriteRecord = new FavoriteRecord();
        if (result != null){
            String region = tokenConfig.getCos().getRegion();
            String bucketName = COSBucketSupport.getFavorBucketName();
            String key = result.getKey();
            String downUrl = String.format("http://%s.cos.%s.myqcloud.com/%s", bucketName, region, key);
            favoriteRecord.setBucket(bucketName);
            favoriteRecord.setCloudUrl(downUrl);
            favoriteRecord.setStatusMsg("已上传");
        }
        String fileName = file.getName();
        favoriteRecord.setFilename(FilenameUtils.getBaseName(fileName));
        favoriteRecord.setFormat(FilenameUtils.getExtension(fileName));
        favoriteRecord.setSize(ByteUtils.toMB(file.length()));
        favoriteRecord.setHost(UrlUtils.getHost(url));
        favoriteRecord.setBaseUrl(url);
        favoriteRecord.setComment(comment);
        favoriteRecord.setUpTime(Timestamp.valueOf(LocalDateTime.now()));
        favoriteRecord.setDownTime(Timestamp.valueOf(LocalDateTime.now()));
        favoriteRecord.setStatus((byte) 0);
        favoriteRecordRepository.save(favoriteRecord);
    }
}
