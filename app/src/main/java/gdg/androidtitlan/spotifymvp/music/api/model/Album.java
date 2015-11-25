package gdg.androidtitlan.spotifymvp.music.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jhordan on 24/11/15.
 */
public class Album {

    @SerializedName("name") public String albumName;

    @SerializedName("images") public List<ArtistImages> trackImages;
}
