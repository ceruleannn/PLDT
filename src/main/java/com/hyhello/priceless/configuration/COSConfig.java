package com.hyhello.priceless.configuration;

import com.hyhello.priceless.config.TokenConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 *
 */
@Configuration
public class COSConfig {

    @Autowired
    @Qualifier("tokenConfig")
    TokenConfig tokenConfig;

    private COSClient client;

    @Bean
    public COSClient getClient(){

        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = tokenConfig.getTencentSecretId();
        String secretKey = tokenConfig.getTencentSecretKey();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-beijing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        client = new COSClient(cred, clientConfig);
        return client;
    }

    @PreDestroy
    public void destroy(){
        if (client != null){
            client.shutdown();
        }
    }
}
