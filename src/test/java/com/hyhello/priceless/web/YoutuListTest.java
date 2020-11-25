package com.hyhello.priceless.web;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.common.collect.Lists;
import com.hyhello.priceless.module.gapi.YoutubeApi;
import com.hyhello.priceless.support.BeanSupport;
import com.hyhello.priceless.web.service.FavoriteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YoutuListTest {

    private static final String DOWN_URL_PREFIX = "https://www.youtube.com/watch?v=";

    @Test
    public void contextLoads() throws GeneralSecurityException, IOException {

        YoutubeApi youtubeApi = BeanSupport.getApplicationContext().getBean(YoutubeApi.class);
        FavoriteService favoriteService = BeanSupport.getApplicationContext().getBean(FavoriteService.class);

        PlaylistListResponse listResponse = youtubeApi.retrievePlayList("", true);
        for (Playlist item : listResponse.getItems()) {
            String title = item.getSnippet().getLocalized().getTitle();
            File dir = new File("D:/local/" + title);
            dir.mkdirs();

            PlaylistItemListResponse itemListResponse  = youtubeApi.retrievePlayListItem(item.getId());
            System.out.println(itemListResponse);

            for (PlaylistItem itemListResponseItem : itemListResponse.getItems()) {
                System.out.println(DOWN_URL_PREFIX + itemListResponseItem.getSnippet().getResourceId().getVideoId());
                favoriteService.addFavorite(DOWN_URL_PREFIX + itemListResponseItem.getSnippet().getResourceId().getVideoId()
                        ,Lists.newArrayList("local"),dir.getPath(),"");
            }
        }
    }
}