package gdg.androidtitlan.spotifymvp.music.view;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Track;

/**
 * Created by Jhordan on 20/11/15.
 */
public interface TracksMvpView extends MvpView {

    void showLoading();

    void hideLoading();

    void showTracksNotFoundMessage();

    void showConnectionErrorMessage();

    void renderTracks(List<Track> tracks);

    void launchTrackDetail(List<Track> tracks,Track track, int position);
}




