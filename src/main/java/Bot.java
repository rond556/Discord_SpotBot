import commands.SpotifySearch;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

    public static void main(String args[]) throws Exception{
        JDA jda = new JDABuilder("Njg0MTUzODQwNzM5NTQ5MjA0.XmZacg.8efmhcHlNm7jrEsnpkuI3v0aiJY").build();

        jda.addEventListener(new SpotifySearch());
    }
}
