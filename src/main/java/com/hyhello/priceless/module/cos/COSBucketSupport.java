package com.hyhello.priceless.module.cos;

import com.hyhello.priceless.support.BeanSupport;
import org.springframework.context.annotation.Bean;

import java.io.File;

/**
 *
 */
public class COSBucketSupport {


    public static String getFavorBucketName(){
        return getAPPBucketName(BeanSupport.getTokenConfig().getCosFavorBucketOriginalName());
    }

    public static String getCDNBucketName(){
        return getAPPBucketName(BeanSupport.getTokenConfig().getCosCDNBucketOriginalName());
    }

    public static String getFileKey(File file, boolean isTimeLimit){
        if (isTimeLimit){
            String prefix = BeanSupport.getTokenConfig().getCosCDNBucketExpirePrefix();
            return prefix + "-" + file.getName();
        }else {
            return file.getName();
        }
    }

    private static String getAPPBucketName (String originalBucketName){
        return originalBucketName + "-" + BeanSupport.getTokenConfig().getCosAppId();
    }
}
