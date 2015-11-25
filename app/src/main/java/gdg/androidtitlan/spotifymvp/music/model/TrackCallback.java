package gdg.androidtitlan.spotifymvp.music.model;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Track;

/**
 * Created by Jhordan on 20/11/15.
 */
public interface TrackCallback {

    void onResponse(List<Track> tracks);

    void onTrackNotFound();

    void onNetworkConnectionError();

    void onServerError();
}
