package com.hyhello.priceless.douyin;


import okhttp3.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件下载工具类（单例模式）
 */

public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    public DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    public void downloadSync(final String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;

            //储存下载文件的目录
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);

            is = response.body().byteStream();
            long total = response.body().contentLength();
            fos = new FileOutputStream(file);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                //下载中更新进度条
                //if (listener != null){
                //    int progress = (int) (sum * 1.0f / total * 100);
                //    listener.onDownloading(progress);
                //}
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }


    public void download(final String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                if (listener != null){
                    listener.onDownloadFailed(e);
                }else {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                //储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);

                try {

                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        //下载中更新进度条
                        if (listener != null){
                            int progress = (int) (sum * 1.0f / total * 100);
                            listener.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    //下载完成
                    if (listener != null){
                        listener.onDownloadSuccess(file);
                    }
                } catch (Exception e) {
                    if (listener != null){
                        listener.onDownloadFailed(e);
                    }else {
                        throw e;
                    }
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }
        });
    }


    public interface OnDownloadListener{

        /**
         * 下载成功之后的文件
         */
        void onDownloadSuccess(File file);

        /**
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载异常信息
         */

        void onDownloadFailed(Exception e);
    }
}
