package com.hyhello.priceless.module.youget;

import com.hyhello.priceless.support.runtime.AbstractRuntimeTask;
import com.hyhello.priceless.support.BeanSupport;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
@RequiredArgsConstructor
public class YouGetRuntimeTask extends AbstractRuntimeTask {

    @NonNull
    private String url;
    private boolean useProxy;

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
            list.stream().forEach(System.out::println);  
        };
    }

    public static void main(String[] args) throws IOException {
//        you-get -s 127.0.0.1:1080 https://www.youtube.com/watch?v=jNQXAC9IVRw
        new YouGetRuntimeTask("https://www.bilibili.com/video/av301").doexec();
    }
}











