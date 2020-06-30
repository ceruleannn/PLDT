package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.support.runtime.AbstractRuntimeTask;
import com.hyhello.priceless.support.runtime.RuntimeSupport;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 选取最佳质量, 返回itag
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class YouGetInfoTask extends AbstractRuntimeTask implements Callable<YouGetQualityInfo> {

    @NonNull
    private String url;

    private boolean useProxy;

    @Override
    public String getCommand() {
        return YouGetCommandBuilder
                .newBuilder()
                .isInfo(true)
                .useProxy(useProxy)
                .setFileUrl(url)
                .build();
    }

    @Override
    public long getTimeoutMillis() {
        return 0;
    }

    @Override
    public YouGetQualityInfo call() throws Exception {
        List<String> lines = RuntimeSupport.exec(this);

        YouGetQualityInfo qualityInfo = new YouGetQualityInfo();
        String currentFormat = null;
        int currentItag = 0;
        int maxSize = 0;

        boolean isDefault = false;
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("[ DEFAULT ]")){
                isDefault = true;
            }
            if (isDefault){
                if (line.startsWith("- itag:")){
                    currentItag = Integer.valueOf(line.substring(8).trim());
                }
                if (line.startsWith("- format:")){
                    currentFormat = line.substring(10).trim();
                }
                if (line.startsWith("size:")){
                    int s = line.indexOf("(");
                    int e = line.indexOf("bytes)");
                    int size = Integer.valueOf(line.substring(s+1, e-1).trim());
                    if (size > maxSize){
                        maxSize = size;
                        qualityInfo.itag = currentItag;
                        qualityInfo.format = currentFormat;
                    }
                }
            }
        }

        log.info("maxItag=" + qualityInfo.itag + " bestformat=" + qualityInfo.format);
        return qualityInfo;
    }
}

class YouGetQualityInfo {
    Integer itag;
    String format;
}









