/**
 * Copyright 2015 Erik Jhordan Rey.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gdg.androidtitlan.spotifymvp.example.interactor;

import gdg.androidtitlan.spotifymvp.example.data.api.client.SpotifyService;
import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import io.reactivex.Observable;
import java.util.List;

public class ArtistsInteractor {

  private SpotifyService spotifyService;

  public ArtistsInteractor(SpotifyService spotifyService) {
    this.spotifyService = spotifyService;
  }

  public Observable<List<Artist>> loadDataFromApi(String query) {
    return spotifyService.search(query);
  }

  //private void onSuccess(ArtistsSearch artistsSearch, ArtistCallback artistCallback) {
  //  if (artistsSearch.getArtists() != null) {
  //    if (artistsSearch.getArtists().size() > 0) {
  //      artistCallback.onResponse(artistsSearch.getArtists());
  //    } else {
  //      artistCallback.onArtistNotFound();
  //    }
  //  } else {
  //    artistCallback.onArtistNotFound();
  //  }
  //}
  //
  //private void onError(Throwable throwable, ArtistCallback artistCallback) {
  //  if (HttpNotFound.isHttp404(throwable)) {
  //    artistCallback.onArtistNotFound();
  //  } else if (throwable.getMessage().equals(HttpNotFound.SERVER_INTERNET_ERROR)) {
  //    artistCallback.onNetworkConnectionError();
  //  } else {
  //    artistCallback.onServerError();
  //  }
  //}
}
