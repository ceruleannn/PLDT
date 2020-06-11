package com.hyhello.priceless.module.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class COSTransferManagerCore {


    private final TransferManager transferManager;

    @Autowired
    public COSTransferManagerCore(COSClient cosClient, TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    public Upload transferManagerUploadObject(String bucketName, File file, String key, boolean isSync) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        Upload upload = transferManager.upload(putObjectRequest);

        if (isSync){
            try {
                upload.waitForCompletion();
            } catch (InterruptedException e) {
                log.error("error while transferManagerUploadObject ", e);
                Thread.currentThread().interrupt();
            }
        }
        return upload;
    }
}
