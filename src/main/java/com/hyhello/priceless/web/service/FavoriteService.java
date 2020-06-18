package com.hyhello.priceless.web.service;

import com.hyhello.priceless.config.TokenConfig;
import com.hyhello.priceless.module.cos.COSBucketSupport;
import com.hyhello.priceless.module.cos.COSService;
import com.hyhello.priceless.module.youget.YouGetService;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.*;

@Service
@Slf4j
public class FavoriteService {

    private final YouGetService youGetService;

    private final COSService cosService;

    private final ThreadPoolExecutor favoriteExecutor;

    private final TokenConfig tokenConfig;

    @Autowired
    public FavoriteService(YouGetService youGetService, COSService cosService, @Qualifier("FavoritePool") ThreadPoolExecutor executor, @Qualifier("tokenConfig") TokenConfig tokenConfig) {
        this.youGetService = youGetService;
        this.cosService = cosService;
        this.favoriteExecutor = executor;
        this.tokenConfig = tokenConfig;
    }

    public void addFavorite(String url){

        Runnable favoriteTask = () -> {
            Future<File> future = youGetService.offerTask(url);
            File file = null;

            boolean ok = true;
            try {
                try {
                    file = future.get(15, TimeUnit.MINUTES);
                } catch (ExecutionException e) {
                    log.error("ExecutionException in addFavorite", e);
                    ok = false;
                } catch (TimeoutException e) {
                    log.error("you-task timeout", e);
                    ok = false;
                }

                UploadResult result = null;
                if (file != null && file.exists()){
                    Upload upload = cosService.uploadFavorite(file);
                    try {
                        upload.waitForCompletion();
                        result = upload.waitForUploadResult();
                    } catch (CosClientException e) {
                        log.error("cos failed", e);
                        ok = false;
                    }
                }

                if (ok){
                    String region = tokenConfig.getCos().getRegion();
                    String bucketName = COSBucketSupport.getFavorBucketName();
                    String key = result.getKey();
                    String downUrl = String.format("http://%s.cos.%s.myqcloud.com/%s", bucketName, region, key);
                }


                //TODO COS LOG
                //TODO REMOVE LOCAL FILE
                //TODO MAKE RECORD
                //TODO COS CLIENT IDLE - Queue

            } catch (InterruptedException e) {
                log.error("InterruptedException in addFavorite", e);
                Thread.currentThread().interrupt();
            }
        };

        favoriteExecutor.submit(favoriteTask);
    }

    private void insertDB(String url, File file){

    }

}
