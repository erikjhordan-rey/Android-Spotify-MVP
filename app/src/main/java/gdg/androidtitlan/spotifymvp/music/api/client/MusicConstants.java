package gdg.androidtitlan.spotifymvp.music.api.client;

/**
 * Created by Jhordan on 20/11/15.
 */
public class MusicConstants {

    public static final String SPOTIFY_API = "https://api.spotify.com";
    public static final String ARTIST_SEARCH = "/v1/search?type=artist";
    public static final String QUERY_SEARCH = "q";
    public static final String PATH_ARTIST_TRACKS = "artistId";
    public static final String ARTIST_TRACKS = "v1/artists/{" + PATH_ARTIST_TRACKS + "}/top-tracks?country=SE";

}
