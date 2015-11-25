package gdg.androidtitlan.spotifymvp.music.view;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Artist;

/**
 * Created by Jhordan on 20/11/15.
 */
public interface ArtistsMvpView extends MvpView {

    void showLoading();

    void hideLoading();

    void showArtistNotFoundMessage();

    void showConnectionErrorMessage();

    void showServerError();

    void renderArtist(List<Artist> artists);

    void launchArtisDetail(Artist artist);
}




