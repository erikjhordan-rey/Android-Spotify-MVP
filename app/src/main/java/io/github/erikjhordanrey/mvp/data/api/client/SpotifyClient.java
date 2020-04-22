package io.github.erikjhordanrey.mvp.data.api.client;

import io.github.erikjhordanrey.mvp.data.api.retrofit.SpotifyRetrofitClient;
import io.github.erikjhordanrey.mvp.data.model.Artist;
import io.github.erikjhordanrey.mvp.data.model.Track;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class SpotifyClient extends SpotifyRetrofitClient implements SpotifyService {

    @Override
    public Observable<List<Artist>> search(String query) {
        return getSpotifyService().searchArtist(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Track>> getTracks(String artistId) {
        return getSpotifyService().getTracks(artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
