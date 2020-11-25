package com.hyhello.priceless.web.service;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.common.collect.Lists;
import com.hyhello.priceless.module.gapi.YoutubeApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * TODO COS EXIST CHECK
 */
@Slf4j
@Service
public class YoutuFavoriteListService {

    @Resource
    FavoriteService favoriteService;

    @Resource
    YoutubeApi youtubeApi;

    private static final String DOWN_URL_PREFIX = "https://www.youtube.com/watch?v=";

    public void execute(String playlistId, List<String> target,String localPath, String comment) throws IOException, GeneralSecurityException {

    }

    public void retrieveMyFavor() throws IOException, GeneralSecurityException {
        PlaylistListResponse listResponse = youtubeApi.retrievePlayList("", true);
        for (Playlist item : listResponse.getItems()) {
            String title = item.getSnippet().getLocalized().getTitle();
            File dir = new File("D:/local/" + title);
            dir.mkdirs();

            PlaylistItemListResponse itemListResponse = youtubeApi.retrievePlayListItem(item.getId());
            System.out.println(itemListResponse);

            for (PlaylistItem itemListResponseItem : itemListResponse.getItems()) {
                System.out.println(DOWN_URL_PREFIX + itemListResponseItem.getSnippet().getResourceId().getVideoId());
                favoriteService.addFavorite(DOWN_URL_PREFIX + itemListResponseItem.getSnippet().getResourceId().getVideoId()
                        , Lists.newArrayList("local"), dir.getPath(), "");
            }
        }
    }
}
