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
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import io.reactivex.Observable;
import java.util.List;

public class TracksInteractor {

  private SpotifyService spotifyService;

  public TracksInteractor(SpotifyService spotifyService) {
    this.spotifyService = spotifyService;
  }

  //You could use RXAndroid instead of a callback to communicate
  // but to keep it simple use a callback and RX only for api calls
  public Observable<List<Track>> loadData(String artistId) {
    return spotifyService.getTracks(artistId);
  }

  //private void onSuccess(Tracks tracks, TrackCallback trackCallback) {
  //  if (tracks.getTracks() != null) {
  //    if (tracks.getTracks().size() > 0) {
  //      trackCallback.onResponse(tracks.getTracks());
  //    } else {
  //      trackCallback.onTrackNotFound();
  //    }
  //  } else {
  //    trackCallback.onTrackNotFound();
  //  }
  //}
  //
  //private void onError(Throwable throwable, TrackCallback trackCallback) {
  //  if (HttpNotFound.isHttp404(throwable)) {
  //    trackCallback.onTrackNotFound();
  //  } else if (throwable.getMessage().equals(HttpNotFound.SERVER_INTERNET_ERROR)) {
  //    trackCallback.onNetworkConnectionError();
  //  } else {
  //    trackCallback.onServerError();
  //  }
  //}
}
