package gdg.androidtitlan.spotifymvp.music.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Artist;
import gdg.androidtitlan.spotifymvp.music.api.model.Artists;

/**
 * Created by Jhordan on 20/11/15.
 */
public class ArtistsSearch {
    @SerializedName("artists") Artists artistsSearch;
    public List<Artist> getArtists() { return artistsSearch.getArtists();}
}
