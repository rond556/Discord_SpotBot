package commands;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class OAuthToken {
    private String authCode = "MmEwYTJhM2NmNTIzNDQ1MmIxMGY3MmI1NDhhODg4YjA6MzlhNTBkYjI2YjU1NGUyMzlkMzIwZTEzNjVjMTU2ZGU=";

    String getOAuthToken() throws IOException {
        String spotifyAccessTokenURL = "https://accounts.spotify.com/api/token";
        String code = "AQC4cb9FFomuRlVydQ3wmD3Uu-m2tk6y8rRPH-_9kDNMGkUY8iGdPM4fnun9SMSx9VmadvHFpLyr-wTLL00RlEgMan2Trh_csxUiJMPDaIVMgq-ycNF8sC__erxVXaj_udmsqNYGPZiHx4I79zyWUktyDDbMzWK8MSQcdUYDZL1enPbiLStLVvtBP4q_3i7Zsa3i85PT-ml8JFe1ERFIwPg_2eA7bXrxG110AdH5b4qoOhJFhn_4tDqi8VAG";
        String redirectURI = "https://rosygamingglassesbackend.herokuapp.com/";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code",code)
                .add("redirect_uri",redirectURI)
                .build();
        Request request= new Request.Builder()
                .url(spotifyAccessTokenURL)
                .addHeader("Authorization","Basic " + authCode)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        assert response.body() != null;
        String jsonResponse = response.body().string();
        System.out.println(jsonResponse);
        return jsonResponse;
    }

    String refreshAccessToken(String refreshToken) throws IOException {
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
