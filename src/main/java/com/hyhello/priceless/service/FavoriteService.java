package com.hyhello.priceless.service;

import com.hyhello.priceless.module.youget.YouGetHandler;
import com.hyhello.priceless.module.youget.YouGetService;
import com.qcloud.cos.COS;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FavoriteService {

    @Autowired
    YouGetService youGetService;

    public void addFavorite(String url){

        YouGetHandler handler = (url1, file) -> {

            //TODO 上传 COS

            //TODO COS HANDLER
            //TODO --- INSERT DB
        };
        youGetService.offerTask(url, handler);

    }

    private void insertDB(String url, File file){

    }

}
