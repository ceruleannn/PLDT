package com.hyhello.priceless;

import com.hyhello.priceless.config.CommonConfig;
import com.hyhello.priceless.module.youget.YouGetRuntimeTask;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(CommonConfig.class)
@EnableJpaRepositories("com.hyhello.priceless.dataaccess.repository")
@ComponentScan("com.hyhello")
@Slf4j
public class PricelessDataApplication {

    public static void main(String[] args) throws IOException {

        ConfigurableApplicationContext context = SpringApplication.run(PricelessDataApplication.class, args);
        //Map<String, TokenConfig> map = context.getBeansOfType(TokenConfig.class);
        //map.values().forEach(System.out::println);

        //TODO RUNNABLE
        YouGetRuntimeTask task = new YouGetRuntimeTask("https://www.bilibili.com/video/av301");
        //task.doexec();

        log.info(MarkerFactory.getMarker("m1"), "debug marker");
        //TRACE , MARKER PATTERN / SEARCH
    }

}
