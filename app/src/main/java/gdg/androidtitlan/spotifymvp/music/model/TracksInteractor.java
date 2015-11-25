package gdg.androidtitlan.spotifymvp.music.model;

import android.content.Context;

import gdg.androidtitlan.spotifymvp.music.api.client.MusicApp;
import gdg.androidtitlan.spotifymvp.music.api.client.MusicService;
import gdg.androidtitlan.spotifymvp.music.api.exception.HttpNotFound;
import gdg.androidtitlan.spotifymvp.music.api.model.Tracks;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Jhordan on 20/11/15.
 */
public class TracksInteractor{

    MusicService musicService;
    MusicApp musicApp;

    public TracksInteractor(Context context) {
        this.musicApp = MusicApp.get(context);
        this.musicService = musicApp.getMusicService();
    }


    public void loadData(String query, TrackCallback trackCallback) {
        musicService.searchTrackList(query)
                .subscribeOn(musicApp.defaultSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Tracks -> onSuccess(Tracks, trackCallback),
                        throwable -> onError(throwable, trackCallback));
    }


    private void onSuccess(Tracks tracks, TrackCallback trackCallback){
        if (tracks.getTracks() != null){
            if(tracks.getTracks().size() > 0)
                trackCallback.onResponse(tracks.getTracks());
            else
                trackCallback.onTrackNotFound();
        }else {
            trackCallback.onTrackNotFound();
        }

    }

    private void onError(Throwable throwable,TrackCallback trackCallback){
        if (HttpNotFound.isHttp404(throwable))
            trackCallback.onTrackNotFound();
        else if (throwable.getMessage().equals(HttpNotFound.SERVER_INTERNET_ERROR))
            trackCallback.onNetworkConnectionError();
        else
            trackCallback.onServerError();
    }




}
