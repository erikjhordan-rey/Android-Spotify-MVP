package gdg.androidtitlan.spotifymvp.music.presenter;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Track;
import gdg.androidtitlan.spotifymvp.music.model.TrackCallback;
import gdg.androidtitlan.spotifymvp.music.model.TracksInteractor;
import gdg.androidtitlan.spotifymvp.music.view.TracksMvpView;

/**
 * Created by Jhordan on 20/11/15.
 */
public class TracksPresenter implements Presenter<TracksMvpView>, TrackCallback {

    private TracksMvpView tracksMvpView;
    private TracksInteractor tracksInteractor;

    public TracksPresenter() {}

    @Override public void attachedView(TracksMvpView view) {

        if (view == null)
            throw new IllegalArgumentException("You can't set a null view");

        tracksMvpView = view;
        tracksInteractor = new TracksInteractor(tracksMvpView.getContext());
    }

    @Override public void detachView() {
        tracksMvpView = null;
    }

    public void onSearchTracks(String string) {
        tracksMvpView.showLoading();
        tracksInteractor.loadData(string, this);
    }

    public void launchArtistDetail(List<Track> tracks,Track track, int position) {
        tracksMvpView.launchTrackDetail(tracks,track,position);
    }

    @Override public void onResponse(List<Track> tracks) {
        tracksMvpView.hideLoading();
        tracksMvpView.renderTracks(tracks);

    }

    @Override public void onTrackNotFound() {
        tracksMvpView.showTracksNotFoundMessage();
    }

    @Override public void onNetworkConnectionError() {
        tracksMvpView.showConnectionErrorMessage();
    }

    @Override public void onServerError() {
        tracksMvpView.showConnectionErrorMessage();
    }
}
