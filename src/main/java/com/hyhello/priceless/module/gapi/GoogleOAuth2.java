package com.hyhello.priceless.module.gapi;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.hyhello.priceless.support.BeanSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

@Component
public class GoogleOAuth2 {

    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");

    private static final String APPLICATION_NAME = "YoutubeApi";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


    private Credential authorizeWithRefreshToken(NetHttpTransport httpTransport, String refreshToken) throws IOException, GeneralSecurityException {
        // Load client secrets.

        InputStream in = YoutubeApi.class.getResourceAsStream(BeanSupport.getTokenConfig().getGoogleAuth2().getClientSecret());
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setRefreshToken(refreshToken);
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
                httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setTokenServerUrl(
                        new GenericUrl("https://oauth2.googleapis.com/token"))
                .setClientAuthentication(new ClientParametersAuthentication(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret()))
                .build()
                .setFromTokenResponse(tokenResponse);

        credential.refreshToken();
        return credential;
    }

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorizeWithLocalAuth(NetHttpTransport httpTransport) throws IOException, GeneralSecurityException {

        // Load client secrets.
        InputStream in = YoutubeApi.class.getResourceAsStream(BeanSupport.getTokenConfig().getGoogleAuth2().getClientSecret());
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        //Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public YouTube getService(boolean useOAuth2) throws GeneralSecurityException, IOException {

        System.setProperty("socksProxyHost", BeanSupport.getCommonConfig().getGlobalSocksProxyHost());
        System.setProperty("socksProxyPort", BeanSupport.getCommonConfig().getGlobalSocksProxyPort());
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Credential credential = useOAuth2 ? authorizeWithRefreshToken(httpTransport, BeanSupport.getTokenConfig().getGoogleAuth2().getRefreshToken()) : null;
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
