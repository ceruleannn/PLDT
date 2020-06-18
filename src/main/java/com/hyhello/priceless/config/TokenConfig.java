package com.hyhello.priceless.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:token.properties")
@EnableConfigurationProperties(TokenConfig.class)
@ConfigurationProperties(prefix="token")
@Data
public class TokenConfig {
    private COS cos = new COS() ;

    private String datasourceUsername;
    private String datasourcePassword;
    private String tencentSecretId;
    private String tencentSecretKey;

    @Data
    public class COS{
        private String favorBucketOriginalName ;
        private String CDNBucketOriginalName;
        private String CDNBucketExpirePrefix ;
        private String CDNBucketWithExpirePrefixExpireDay;
        private String region;
        private String appId;
    }

}
