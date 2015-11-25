package gdg.androidtitlan.spotifymvp.music.model;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Artist;

/**
 * Created by Jhordan on 20/11/15.
 */
public interface ArtistCallback {

    void onResponse(List<Artist> artists);

    void onArtistNotFound();

    void onNetworkConnectionError();

    void onServerError();
}
