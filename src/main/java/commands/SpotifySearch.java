package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SpotifySearch extends ListenerAdapter {

    public void onGuildMessageReceived (GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");

        if(messageSent[0].equalsIgnoreCase("!spotbot") && messageSent.length >= 2){
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < messageSent.length; i++){
                sb.append(messageSent[i]);
                sb.append("%20");
            }
            String spotifySearchURL = "https://api.spotify.com/v1/search?q=";
            String searchQuery = sb.toString().substring(0,sb.toString().length() - 3).toLowerCase();
            String queryType = "&type=track";
            event.getChannel().sendMessage(spotifySearchURL + searchQuery + queryType).queue();
        }

    }
}
