package commands;

import okhttp3.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PlaylistTool {
    private String refreshToken = "AQAgbJvPteF3WhH49shn4BvVrGriCK093rd5KHJMLikBeQY1PSabiMP1ygAGl8W7-keU-e4UkQUjreJnA3Ct4oL0QVYIt0Uxo5zTSfAtYHsSjLYVzV2gEczqEOnxEnClDMQ";
    private String userId = "pvnnm9axgls1e65xv2igs2avg";
    private String playlistId = "0G9dupEvvRkt1kFRx6ChIX";

    public void addSongToPlaylist(String trackURI) throws IOException {
        OAuthToken oAuthToken = new OAuthToken();
        String token = oAuthToken.refreshAccessToken(refreshToken);
        String spotifyURL = "https://api.spotify.com/v1/users/" + userId + "/playlists/" + playlistId + "/tracks";
        URL url = null;
        try {
            url = new URL(spotifyURL);
        } catch (MalformedURLException e){
            System.out.println("Malformed URL");
        }
        String refreshResponse = makeAPIPostRequest(trackURI, token, url);
        System.out.println(refreshResponse);
    }

    private String makeAPIPostRequest(String trackURI, String token, URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("uris", trackURI)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assert response.body() != null;
        return response.body().string();
    }
}

