package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class YouGetService {

    @Autowired
    @Qualifier("YouGetPool")
    ThreadPoolExecutor threadPool;

    public Future<File> offerTask(YouGetRuntimeTask task){
       return threadPool.submit(task);
    }

    public Future<File> offerTask(String resourceUrl){

        //TODO PROXY-HOST
        boolean useProxy = UrlUtils.getHost(resourceUrl).equals("youtube.com");
        return this.offerTask(new YouGetRuntimeTask(resourceUrl, useProxy));
    }
}
