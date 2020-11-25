package com.hyhello.priceless.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Bean("YouGetPool")
    public ThreadPoolExecutor getYouGetThreadPool() {

        RejectedExecutionHandler handler = (runnable, executor)->{
            log.error("YouGetPool Executor is Full ");
        };
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(500),
                handler
        );
    }

    @Bean("FavoritePool")
    public ThreadPoolExecutor getFavoritePoolThreadPool() {

        RejectedExecutionHandler handler = (runnable, executor)->{
            log.error("FavoritePool Executor is Full ");
        };

        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(500),
                handler
        );
    }
}
