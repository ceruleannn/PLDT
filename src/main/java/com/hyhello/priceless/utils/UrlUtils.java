package com.hyhello.priceless.utils;

import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class UrlUtils {

    public static String getHost(String link){
        try {
            URL url = new URL(link);
            return url.getHost();
        } catch (MalformedURLException e) {
            return "";
        }
    }
}
