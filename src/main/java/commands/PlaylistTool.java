package commands;

public class PlaylistTool {


    public void addSongToPlaylist(String trackURI){
        String completeCurlCall = createCurlCall(trackURI);
        System.out.println(completeCurlCall);
    }

    private String createCurlCall(String trackURI) {
        String url = "https://api.spotify.com/v1/playlists/";
        String playlistId = "2e6mUG19vuOG9WcnHR49EA/tracks?uris=spotify%3Atrack%3A";
        String totalURLCall = url + playlistId + trackURI;
        String curlCall = "curl -X \"POST\" \"" + totalURLCall + "\" -H \"Accept: application/json\" -H \"Content-Type: application/json\" -H \"Authorization: Bearer";
        return curlCall;
    }
}
