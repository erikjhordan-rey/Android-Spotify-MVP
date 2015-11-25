package gdg.androidtitlan.spotifymvp.music.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jhordan on 23/11/15.
 */
public class Tracks {
    @SerializedName("tracks") private List<Track> tracks;
    public List<Track> getTracks() {return tracks;}
}
