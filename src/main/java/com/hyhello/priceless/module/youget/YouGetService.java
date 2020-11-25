package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.dataaccess.entity.FavoriteFilter;
import com.hyhello.priceless.dataaccess.repository.FavoriteFilterRepository;
import com.hyhello.priceless.utils.JavaUtils;
import com.hyhello.priceless.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class YouGetService {

    @Autowired
    @Qualifier("YouGetPool")
    ThreadPoolExecutor threadPool;

    @Autowired
    private FavoriteFilterRepository favoriteFilterRepository;

    public <T> Future<T> offerTask(Callable<T> task){
       return threadPool.submit(task);
    }

    public Future<File> doTask(String resourceUrl, String localPath) {

        //TODO CACHE?
        Map<String, FavoriteFilter> map = favoriteFilterRepository.findAll().stream().collect(Collectors.toMap(FavoriteFilter::getHost, Function.identity()));

        boolean useProxy = false;
        FavoriteFilter favoriteFilter = map.get(UrlUtils.getHost(resourceUrl));
        if (favoriteFilter != null){
            useProxy = JavaUtils.parseNumber2Boolean(favoriteFilter.getUseProxy());
        }

        YouGetQualityInfo info = null;
        try {
            info = this.offerTask(new YouGetInfoTask(resourceUrl, useProxy)).get();
            if (info == null){
                log.warn("fail getting YouGetQualityInfo:" + resourceUrl);
                return null;
            }
        } catch (InterruptedException e) {
            log.warn("InterruptedException in addFavorite", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.warn("ExecutionException in addFavorite", e);
        }

        return this.offerTask(new YouGetRuntimeTask(resourceUrl,localPath, useProxy, info));
    }
}
