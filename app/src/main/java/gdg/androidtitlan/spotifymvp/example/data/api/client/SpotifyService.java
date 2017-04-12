package gdg.androidtitlan.spotifymvp.example.data.api.client;

import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import io.reactivex.Observable;
import java.util.List;

public interface SpotifyService {

  Observable<List<Artist>> search(String query);

  Observable<List<Track>> getTracks(String artistId);
}
