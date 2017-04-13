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

import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import gdg.androidtitlan.spotifymvp.example.interactor.ArtistsInteractor;
import io.reactivex.disposables.Disposable;
import java.util.List;

public class ArtistsPresenter extends Presenter<ArtistsPresenter.View> {

  private ArtistsInteractor interactor;

  public ArtistsPresenter(ArtistsInteractor interactor) {
    this.interactor = interactor;
  }

  public void onSearchArtist(String name) {
    getView().showLoading();
    Disposable disposable = interactor.searchArtists(name).subscribe(artists -> {
      if (!artists.isEmpty() && artists.size() > 0) {
        getView().hideLoading();
        getView().renderArtists(artists);
      } else {
        getView().showArtistNotFoundMessage();
      }
    }, Throwable::printStackTrace);

    addDisposableObserver(disposable);
  }

  public void launchArtistDetail(Artist artist) {
    getView().launchArtistDetail(artist);
  }

  @Override public void terminate() {
    super.terminate();
    setView(null);
  }

  public interface View extends Presenter.View {

    void showLoading();

    void hideLoading();

    void showArtistNotFoundMessage();

    void showConnectionErrorMessage();

    void showServerError();

    void renderArtists(List<Artist> artists);

    void launchArtistDetail(Artist artist);
  }
}
