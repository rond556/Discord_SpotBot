package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class SpotifySearch extends ListenerAdapter {
    private String refreshToken = "AQCKvMQEdS-UeHYbAgbiVmZitPoS0RaWqYTMUH5K6-uNGNwUH1lvVKMeCVMJQAMO5CfbW3wGxJ1JP7w_EEFHuZ-YOxl8D1yKC0HdlR15xw_QPZxuUeKi3nNh0eKqvhFk7uk";


    public void onGuildMessageReceived (GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        if(messageSent[0].equalsIgnoreCase("!spotbot") && messageSent.length >= 2) {
            try {
                makeAPICall(messageSent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeAPICall(String[] messageSent) throws IOException {
        OAuthToken oAuthToken = new OAuthToken();
        String token = oAuthToken.refreshAccessToken(refreshToken);
        PlaylistTool playlistTool = new PlaylistTool();
        URL url = new URL(getRequestURL(messageSent));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization","Bearer " + token)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        assert response.body() != null;
        String jsonResponse = response.body().string();
        System.out.println(jsonResponse);
        String trackUri = parseJSONResponseForURI(jsonResponse);
        playlistTool.addSongToPlaylist(trackUri);
    }

    private String parseJSONResponseForURI(String jsonResponse) {
        String trackUri = "";
        JSONParser jsonParser = new JSONParser();
        Object jsonObject = null;
        try {
            jsonObject = jsonParser.parse(jsonResponse);
        }catch(ParseException e){
            System.out.println("Parse error");
        }
        JSONObject jsonResults = (JSONObject) jsonObject;
        JSONObject parent =(JSONObject) jsonResults.get("tracks");
        JSONArray items = (JSONArray) parent.get("items");
        for(Object obj : items){
            StringBuilder sb = new StringBuilder();
            sb.append(((JSONObject) obj).get("uri"));
            trackUri = sb.toString();
        }
        return trackUri;
    }

    private String getRequestURL(String[] messageSent) {
        String spotifySearchURL = "https://api.spotify.com/v1/search?q=";
        StringBuilder sb = new StringBuilder();
        String queryType = "&type=track&limit=1";
        for(int i = 1; i < messageSent.length; i++){
            sb.append(messageSent[i]);
            sb.append("%20");
        }
        String searchQuery = sb.toString().substring(0,sb.toString().length() - 3).toLowerCase();
        return spotifySearchURL + searchQuery + queryType;
    }

}
