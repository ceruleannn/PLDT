package com.hyhello.priceless.module.cos;

import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class COSService {

    private COSTransferManagerCore cosTransferManagerCore;

    @Autowired
    public COSService(COSTransferManagerCore cosTransferManagerCore) {
        this.cosTransferManagerCore = cosTransferManagerCore;
    }

    /**
     * 上传到收藏bucket
     * @param file
     * @return
     */
    public Upload uploadFavorite(File file){
        String bucketName = COSBucketSupport.getFavorBucketName();
        String key = COSBucketSupport.getFileKey(file, false);
        return cosTransferManagerCore.transferManagerUploadObject(bucketName, file, key, false);
    }

    /**
     * 上传到CDN加速 bucket
     * @param file
     * @return
     */
    public Upload uploadCDN(File file){
        String bucketName = COSBucketSupport.getCDNBucketName();
        String key = COSBucketSupport.getFileKey(file, false);
        return cosTransferManagerCore.transferManagerUploadObject(bucketName, file, key, false);
    }


    public Upload uploadCDNExpire(File file){
        String bucketName = COSBucketSupport.getCDNBucketName();
        String key = COSBucketSupport.getFileKey(file, true);
        return cosTransferManagerCore.transferManagerUploadObject(bucketName, file, key, false);
    }
}
