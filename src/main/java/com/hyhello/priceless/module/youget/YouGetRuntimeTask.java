package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.support.runtime.AbstractRuntimeTask;
import com.hyhello.priceless.support.BeanSupport;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class YouGetRuntimeTask extends AbstractRuntimeTask {

    @NonNull
    private String url;

    private boolean useProxy;

    @NonNull
    private YouGetHandler handler;

    @Override
    public String getcmd() {
        return YouGetCommandBuilder
                .newBuilder()
                .setRootPath(BeanSupport.getCommonConfig().getYougetTempDir())
                .useProxy(useProxy)
                .setFileUrl(url)
                .build();
    }

    @Override
    public long getTimeoutMillis() {
        return 0;
    }

    @Override
    public Consumer<List<String>> getCallback() {
        return list -> {

            String fileName = null;
            String title = null;
            for (String line : list) {
                if (line.startsWith("title")) {
                    title = line.substring(7).trim();
                }

                if (line.startsWith("Downloading ") && fileName == null) {
                    int index = line.indexOf("...");
                    fileName = line.substring(12, index);
                }
            }

            String path = BeanSupport.getCommonConfig().getYougetTempDir()  + fileName;
            File file = new File(path);
            if (file.exists() && !StringUtils.isEmpty(fileName)){
                log.info("YouGetTask success: " + url);
                handler.handle(url, file);
            }else {
                log.warn("YouGetTask failed: " + url);
            }
        };
    }

}











