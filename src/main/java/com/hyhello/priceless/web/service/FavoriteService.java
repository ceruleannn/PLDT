package com.hyhello.priceless.web.service;

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

    private final ThreadPoolExecutor executor;

    @Autowired
    public FavoriteService(YouGetService youGetService, COSService cosService, @Qualifier("FavoritePool") ThreadPoolExecutor executor) {
        this.youGetService = youGetService;
        this.cosService = cosService;
        this.executor = executor;
    }

    public void addFavorite(String url){

        Runnable favoriteTask = () -> {
            Future<File> future = youGetService.offerTask(url);
            File file = null;
            try {
                try {
                    file = future.get(15, TimeUnit.MINUTES);
                } catch (ExecutionException e) {
                    log.error("ExecutionException in addFavorite", e);
                    return;
                } catch (TimeoutException e) {
                    log.error("you-task timeout", e);
                    return;
                }

                if (file != null){
                    Upload upload = cosService.uploadFavorite(file);
                    try {
                        upload.waitForCompletion();
                        UploadResult result = upload.waitForUploadResult();
                    } catch (CosClientException e) {
                        log.error("cos failed", e);
                    }
                }

                //TODO REMOVE LOCAL FILE
                //TODO MAKE RECORD
            } catch (InterruptedException e) {
                log.error("InterruptedException in addFavorite", e);
                Thread.currentThread().interrupt();
            }
        };

        executor.submit(favoriteTask);

        //TODO --- INSERT DB
    }

    private void insertDB(String url, File file){

    }

}
