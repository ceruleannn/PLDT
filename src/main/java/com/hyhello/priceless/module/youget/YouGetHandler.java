package com.hyhello.priceless.module.youget;

import java.io.File;

/**
 *
 */
public interface YouGetHandler {

    /**
     * handle the you-get task file;
     * @param file
     */
    void handle(String url, File file);

}
