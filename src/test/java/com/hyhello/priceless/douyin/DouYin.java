package com.hyhello.priceless.douyin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 *
 */
public class DouYin {

    public static void main(String[] args) throws IOException {
        crawlFavorite();
    }

    public static void crawlFavorite() throws IOException {
        
        File dir  = new File("C:\\Users\\Administrator\\Desktop\\dyff");
        File[] jsons = dir.listFiles();
        for (File file : jsons) {
            String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode aweme_list = root.get("aweme_list");
            for (JsonNode ele : aweme_list) {
                for (JsonNode urlnode : ele.get("video").get("download_addr").get("url_list")) {
                    String url = urlnode.asText();
                    if (url.startsWith("http://v3-dy-o.zjcdn.com/")){
                        File store = new File("C:\\Users\\Administrator\\Desktop\\dyff\\" + UUID.randomUUID() + ".mp4");
                        store.createNewFile();

                        FileUtils.copyURLToFile(new URL(url), store);
                        System.out.println(url);
                    }
                }
            }
        }
    }
}
