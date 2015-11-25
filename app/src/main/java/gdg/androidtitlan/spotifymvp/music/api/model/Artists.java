package gdg.androidtitlan.spotifymvp.music.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jhordan on 20/11/15.
 */
public class Artists {
    @SerializedName("items") private List<Artist> artists;
    public List<Artist> getArtists() { return artists;}
}
