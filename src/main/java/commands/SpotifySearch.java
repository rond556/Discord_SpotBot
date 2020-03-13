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
    private String refreshToken = "";


    public void onGuildMessageReceived (GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split("%%%");
        if(messageSent[0].equalsIgnoreCase("!spotbot") && messageSent.length >= 3) {
            try {
                makeAPICall(messageSent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(messageSent[0].equalsIgnoreCase("!spotbot") && messageSent.length <= 1){
            event.getChannel().sendMessage("Please use the format \"!spotbot%%%song title\" \n or \n \"!spotbot%%%song title%%%artist name\".").queue();
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
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.spotify.com/v1/search?q=track:");


        sb.append(splitTrackAndArtist(messageSent[1].toLowerCase()));
        if(messageSent.length == 3){
            sb.append("artist:");
            sb.append(splitTrackAndArtist(messageSent[2].toLowerCase()));
        }
        sb.append("&type=track&limit=1");
        return sb.toString();
    }

    private String splitTrackAndArtist(String s1) {
        String[] trackName = s1.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String s : trackName){
            sb.append(s);
            sb.append("%20");
        }
        return sb.toString();
    }

}
