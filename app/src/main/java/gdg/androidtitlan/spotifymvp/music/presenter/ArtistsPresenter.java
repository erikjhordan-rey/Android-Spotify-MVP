package gdg.androidtitlan.spotifymvp.music.presenter;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.model.ArtistCallback;
import gdg.androidtitlan.spotifymvp.music.api.model.Artist;
import gdg.androidtitlan.spotifymvp.music.model.ArtistsInteractor;
import gdg.androidtitlan.spotifymvp.music.view.ArtistsMvpView;

/**
 * Created by Jhordan on 20/11/15.
 */
public class ArtistsPresenter implements Presenter<ArtistsMvpView>, ArtistCallback {

    private ArtistsMvpView artistsMvpView;
    private ArtistsInteractor artistsInteractor;

    public ArtistsPresenter(){}

    @Override public void attachedView(ArtistsMvpView view) {
        if (view == null)
            throw new IllegalArgumentException("You can't set a null view");

        artistsMvpView = view;
        artistsInteractor = new ArtistsInteractor(artistsMvpView.getContext());
    }

    @Override public void detachView() {
        artistsMvpView = null;
    }

    public void onSearchArtist(String string){
        artistsMvpView.showLoading();
        artistsInteractor.loadDataFromApi(string, this);
    }

    public void launchArtistDetail(Artist artist){
        artistsMvpView.launchArtisDetail(artist);
    }

    @Override public void onResponse(List<Artist> artists) {
        artistsMvpView.hideLoading();
        artistsMvpView.renderArtist(artists);
    }

    @Override public void onArtistNotFound() {
        artistsMvpView.showArtistNotFoundMessage();
    }


    @Override public void onNetworkConnectionError() {
        artistsMvpView.showConnectionErrorMessage();
    }


    @Override public void onServerError() {
        artistsMvpView.showServerError();
    }
}
