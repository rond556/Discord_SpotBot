package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpotifySearch extends ListenerAdapter {

    public void onGuildMessageReceived (GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        if(messageSent[0].equalsIgnoreCase("!spotbot") && messageSent.length >= 2) {
            try {
                makeAPICall(messageSent, event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void makeAPICall(String[] messageSent, GuildMessageReceivedEvent event) throws IOException {
        URL url = new URL(getRequestURL(messageSent));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        StringBuilder fullResponse = new StringBuilder();
        fullResponse.append(connection.getResponseCode())
                .append(" ")
                .append(connection.getResponseMessage())
                .append("\n");

        event.getChannel().sendMessage(fullResponse.toString()).queue();
    }

    private String getRequestURL(String[] messageSent) {
        String spotifySearchURL = "https://api.spotify.com/v1/search?q=";
        StringBuilder sb = new StringBuilder();
        String queryType = "&type=track";
        for(int i = 1; i < messageSent.length; i++){
            sb.append(messageSent[i]);
            sb.append("%20");
        }
        String searchQuery = sb.toString().substring(0,sb.toString().length() - 3).toLowerCase();
        return spotifySearchURL + searchQuery + queryType;
    }
}
