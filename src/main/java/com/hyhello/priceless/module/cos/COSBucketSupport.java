package com.hyhello.priceless.module.cos;

import com.hyhello.priceless.support.BeanSupport;
import org.springframework.context.annotation.Bean;

import java.io.File;

/**
 *
 */
public class COSBucketSupport {


    public static String getFavorBucketName(){
        return getAPPBucketName(BeanSupport.getTokenConfig().getCos().getFavorBucketOriginalName());
    }

    public static String getCDNBucketName(){
        return getAPPBucketName(BeanSupport.getTokenConfig().getCos().getCDNBucketOriginalName());
    }

    public static String getFileKey(File file, boolean isTimeLimit){
        if (isTimeLimit){
            String prefix = BeanSupport.getTokenConfig().getCos().getCDNBucketExpirePrefix();
            return prefix + "-" + file.getName().trim();
        }else {
            return file.getName().trim();
        }
    }

    private static String getAPPBucketName (String originalBucketName){
        return originalBucketName + "-" + BeanSupport.getTokenConfig().getCos().getAppId();
    }
}
