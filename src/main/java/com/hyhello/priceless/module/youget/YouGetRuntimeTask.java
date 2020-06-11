package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.support.runtime.AbstractRuntimeTask;
import com.hyhello.priceless.support.BeanSupport;
import com.hyhello.priceless.support.runtime.RuntimeSupport;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class YouGetRuntimeTask extends AbstractRuntimeTask implements Callable<File> {

    @NonNull
    private String url;

    private boolean useProxy;

    @Override
    public String getCommand() {
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
    public File call() throws Exception {
        List<String> lines = RuntimeSupport.exec(this);

        String fileName = null;
        String title = null;
        for (String line : lines) {
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
            return file;
        }else {
            log.warn("YouGetTask failed: " + url);
            return null;
        }
    }
}











