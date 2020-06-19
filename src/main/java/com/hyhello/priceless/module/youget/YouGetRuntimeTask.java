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

        List<String> fileNames = new ArrayList<>();
        String title = null;
        if (lines != null){
            for (String line : lines) {
                if (line.startsWith("title")) {
                    title = line.substring(7).trim();
                }

                if (line.startsWith("Downloading ")) {
                    int index = line.indexOf("...");
                    fileNames.add(line.substring(12, index));
                }

                //srt in youtube
                if (line.startsWith("Saving ")) {
                    int index = line.indexOf("...");
                    fileNames.add(line.substring(7, index));
                }
            }
        }


        File refile = null;
        if (fileNames.size() > 0){
            for (int index=0; index<fileNames.size(); index++) {
                String fileName = fileNames.get(index);
                String path = BeanSupport.getCommonConfig().getYougetTempDir()  + fileName;
                File file = new File(path);

                //无法用title比对: title = Me at the zoo ; Saving Me at the zoo.en.srt
                if (index == 0){
                    if (file.exists() && !StringUtils.isEmpty(fileName)){
                        refile = file;
                    }else {
                        log.warn("YouGetTask failed: " + url);
                        return null;
                    }
                }else {
                    if (file.exists()){
                        log.info("delete you-get irrelevant file: " + file.getName());
                        file.delete();
                    }
                }
            }
        }else {
            log.warn("YouGetTask failed: " + url);
        }

        return refile;
    }
}











