package com.hyhello.priceless.module.gapi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.hyhello.priceless.support.BeanSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class YoutubeApi {

    @Resource
    GoogleOAuth2 googleOAuth;

    /**
     * 非OAuth2验证自己仅能获得公开的列表
     * @param channelId
     * @return
     * @throws IOException
     */
     public PlaylistListResponse retrievePlayList(String channelId, boolean mine) throws IOException, GeneralSecurityException {
        YouTube youtubeService = googleOAuth.getService(true);
        YouTube.Playlists.List request = youtubeService.playlists()
                .list("snippet,contentDetails");
        request.setMaxResults(25L).setKey(BeanSupport.getTokenConfig().getGoogleAuth2().getAPI_KEY());
        if (mine){
            request.setMine(mine);
        }else {
            request.setChannelId(channelId);
        }
        return request.execute();
    }


    public PlaylistItemListResponse retrievePlayListItem(String playListId) throws IOException, GeneralSecurityException {
        YouTube youtubeService = googleOAuth.getService(true);
        YouTube.PlaylistItems.List request = youtubeService.playlistItems()
                .list("snippet,contentDetails");
        PlaylistItemListResponse response = request.setMaxResults(25L)
                .setKey(BeanSupport.getTokenConfig().getGoogleAuth2().getAPI_KEY())
                .setPlaylistId(playListId)
                .execute();
        return response;
    }
}
