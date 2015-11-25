package gdg.androidtitlan.spotifymvp.music.model;

import android.content.Context;


import gdg.androidtitlan.spotifymvp.music.api.client.MusicApp;
import gdg.androidtitlan.spotifymvp.music.api.client.MusicService;
import gdg.androidtitlan.spotifymvp.music.api.exception.HttpNotFound;
import gdg.androidtitlan.spotifymvp.music.api.model.ArtistsSearch;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Jhordan on 20/11/15.
 */
public class ArtistsInteractor{

    MusicService musicService;
    MusicApp musicApp;

    public ArtistsInteractor(Context context) {
        this.musicApp = MusicApp.get(context);
        this.musicService = musicApp.getMusicService();
    }


    public void loadDataFromApi(String query, ArtistCallback artistCallback) {
        musicService.searchArtist(query)
                .subscribeOn(musicApp.defaultSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ArtistsSearch -> onSuccess(ArtistsSearch, artistCallback),
                           throwable -> onError(throwable, artistCallback));
    }


    private void onSuccess(ArtistsSearch artistsSearch, ArtistCallback artistCallback){
        if (artistsSearch.getArtists() != null){
            if(artistsSearch.getArtists().size() > 0)
                artistCallback.onResponse(artistsSearch.getArtists());
            else
                artistCallback.onArtistNotFound();
        }else {
            artistCallback.onArtistNotFound();
        }

    }

    private void onError(Throwable throwable,ArtistCallback artistCallback){
        if (HttpNotFound.isHttp404(throwable))
            artistCallback.onArtistNotFound();
        else if (throwable.getMessage().equals(HttpNotFound.SERVER_INTERNET_ERROR))
            artistCallback.onNetworkConnectionError();
        else
            artistCallback.onServerError();
    }




}
