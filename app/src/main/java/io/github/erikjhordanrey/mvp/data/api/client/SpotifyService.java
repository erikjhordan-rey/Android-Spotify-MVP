package io.github.erikjhordanrey.mvp.data.api.client;

import io.github.erikjhordanrey.mvp.data.model.Artist;
import io.github.erikjhordanrey.mvp.data.model.Track;
import io.reactivex.Observable;
import java.util.List;

public interface SpotifyService {

  Observable<List<Artist>> search(String query);

  Observable<List<Track>> getTracks(String artistId);
}
