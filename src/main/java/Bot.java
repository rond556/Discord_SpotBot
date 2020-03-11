import commands.SpotifySearch;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

    public static void main(String args[]) throws Exception{
        JDA jda = new JDABuilder("Njg0MTUzODQwNzM5NTQ5MjA0.Xmk8Pw.4b_m7AUyNZ3TLMi4dEaJtiz1qN8").build();

        jda.addEventListener(new SpotifySearch());
    }
}
