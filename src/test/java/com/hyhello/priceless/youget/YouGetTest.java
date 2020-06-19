package com.hyhello.priceless.youget;

import com.hyhello.priceless.support.runtime.RuntimeSupport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public class YouGetTest {
    public static void main(String[] args) throws IOException, InterruptedException {

        Consumer<List<String>> callback2 = list -> {

            String filename = null;
            boolean succ = false;
            for (String s: list) {
                System.out.println(s);
                if (s.startsWith("Downloading ")) {
                    int index = s.indexOf("...");
                    filename = s.substring(12,index);
                    System.out.println(filename);
                }

                if (s.contains("100%")) {
                    succ = true;
                }
            }

            if (succ) {
                File file = new File("C:\\Users\\Administrator\\Desktop\\" + filename);
                if (file.exists()) {
                    System.out.println(file.length());
                }
            }
        };


        RuntimeSupport.exec("you-get -o C:\\Users\\Administrator\\Desktop https://www.bilibili.com/video/av298", 1, callback2);
    }
}
