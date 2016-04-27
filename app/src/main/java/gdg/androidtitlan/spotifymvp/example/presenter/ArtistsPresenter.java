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

package gdg.androidtitlan.spotifymvp.example.presenter;

import java.util.List;

import gdg.androidtitlan.spotifymvp.example.model.ArtistCallback;
import gdg.androidtitlan.spotifymvp.example.api.model.Artist;
import gdg.androidtitlan.spotifymvp.example.model.ArtistsInteractor;
import gdg.androidtitlan.spotifymvp.example.view.ArtistsMvpView;

public class ArtistsPresenter implements Presenter<ArtistsMvpView>, ArtistCallback {

  private ArtistsMvpView artistsMvpView;
  private ArtistsInteractor artistsInteractor;

  public ArtistsPresenter() {
  }

  @Override public void setView(ArtistsMvpView view) {
    if (view == null) throw new IllegalArgumentException("You can't set a null view");

    artistsMvpView = view;
    artistsInteractor = new ArtistsInteractor(artistsMvpView.getContext());
  }

  @Override public void detachView() {
    artistsMvpView = null;
  }

  public void onSearchArtist(String string) {
    artistsMvpView.showLoading();
    artistsInteractor.loadDataFromApi(string, this);
  }

  public void launchArtistDetail(Artist artist) {
    artistsMvpView.launchArtistDetail(artist);
  }

  @Override public void onResponse(List<Artist> artists) {
    artistsMvpView.hideLoading();
    artistsMvpView.renderArtists(artists);
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
