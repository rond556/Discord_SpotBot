public class Track {
    private String title;
    private String artist;
    private String urlLink;

    public Track() {
    }

    public Track(String title, String artist, String urlLink) {
        this.title = title;
        this.artist = artist;
        this.urlLink = urlLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }
}
