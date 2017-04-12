package gdg.androidtitlan.spotifymvp.example.data.api.client;

import gdg.androidtitlan.spotifymvp.example.data.api.retrofit.SpotifyRetrofitClient;
import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class SpotifyClient extends SpotifyRetrofitClient implements SpotifyService {

  @Override public Observable<List<Artist>> search(String query) {
    return getSpotifyService().searchArtist(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Override public Observable<List<Track>> getTracks(String artistId) {
    return getSpotifyService().getTracks(artistId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
