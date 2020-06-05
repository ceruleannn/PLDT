package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

@Service
public class YouGetService {

    @Autowired
    @Qualifier("YouGetPool")
    ThreadPoolExecutor threadPool;

    public void offerTask(YouGetRuntimeTask task){
        threadPool.execute(task);
    }

    public void offerTask(String resourceUrl, YouGetHandler handler){

        //TODO PROXY-HOST
        boolean useProxy = UrlUtils.getHost(resourceUrl).equals("youtube.com");

        this.offerTask(new YouGetRuntimeTask(resourceUrl, useProxy , handler));
    }
}
