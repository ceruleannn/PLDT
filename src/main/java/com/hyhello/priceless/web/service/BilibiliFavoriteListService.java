package com.hyhello.priceless.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO COS EXIST CHECK
 */
@Slf4j
@Service
public class BilibiliFavoriteListService {

    @Resource
    FavoriteService favoriteService;

    private static final String CRAWL_URL = "https://api.bilibili.com/x/v1/medialist/resource/list?type=3&biz_id=%s&offset_index=%d&offset=%d&from=web&first_page=false&ps=20";
    private static final String DOWN_URL_PREFIX = "https://www.bilibili.com/video/";

    public void execute(int biz_id, List<String> target, String localPath, String comment) throws IOException {
        List<String> bv_ids = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        int offset = 0;
        while (offset != -1){
            String fetchUrl = String.format(CRAWL_URL, String.valueOf(biz_id), offset, offset);
            String result = restTemplate.getForObject(fetchUrl, String.class);
            System.out.println(result);

            JsonNode root = objectMapper.readTree(result);
            int nextOffset = root.get("data").get("nextOffset").asInt();
            if (nextOffset != offset + 20){
                offset = -1;
            }else {
                offset = nextOffset;
            }

            for (JsonNode node : root.get("data").get("mediaList")) {
                String link = node.get("bv_id").asText();
                bv_ids.add(link);
            }
        }

        log.info("bilibili list size = " + bv_ids.size());
        for (String bv_id : bv_ids) {
            favoriteService.addFavorite(DOWN_URL_PREFIX + bv_id, target, localPath, comment);
        }
    }
}
