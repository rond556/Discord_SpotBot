package commands;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class OAuthToken {
    private String authCode = "";

    public String refreshAccessToken(String refreshToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody refreshBody = new FormBody.Builder()
                .add("grant_type","refresh_token")
                .add("refresh_token",refreshToken)
                .build();
        Request refreshRequest = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .addHeader("Authorization","Basic " + authCode)
                .post(refreshBody)
                .build();
        Call call = client.newCall(refreshRequest);
        Response response = call.execute();
        assert response.body() != null;
        String refreshResponse = response.body().string();
        return parseRefreshResponse(refreshResponse);
    }

    @NotNull
    private String parseRefreshResponse(String refreshResponse) {
        JSONParser parser = new JSONParser();
        Object refreshedJSON = null;
        try{
            refreshedJSON = parser.parse(refreshResponse);
        } catch (ParseException e){
            System.out.println("Parse Exception");
        }
        JSONObject parseResults = (JSONObject) refreshedJSON;
        StringBuilder sb = new StringBuilder();
        assert parseResults != null;
        sb.append(parseResults.get("access_token"));
        return sb.toString();
    }
}
