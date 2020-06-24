package com.hyhello.priceless.fileconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:common.properties")
@EnableConfigurationProperties(CommonConfig.class)
@ConfigurationProperties(prefix="common")
@Data
public class CommonConfig {

    private String env;
    private String yougetProxyType;
    private String yougetProxyAddress;
    private String yougetTempDir;

}
