/**
 * Copyright 2016 Erik Jhordan Rey.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gdg.androidtitlan.spotifymvp;

import gdg.androidtitlan.spotifymvp.data.FakeSpotifyAPI;
import gdg.androidtitlan.spotifymvp.example.api.client.SpotifyApp;
import gdg.androidtitlan.spotifymvp.example.api.client.SpotifyService;
import gdg.androidtitlan.spotifymvp.example.api.model.Artist;
import gdg.androidtitlan.spotifymvp.example.api.model.ArtistsSearch;
import gdg.androidtitlan.spotifymvp.example.presenter.ArtistsPresenter;
import gdg.androidtitlan.spotifymvp.example.view.ArtistsMvpView;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//These tests are a clear example of bad
// architecture not allow create real test, this test are only simulation

@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public class ArtistsPresenterTest {

  @Mock private ArtistsMvpView mArtistsMvpView;
  @Mock private SpotifyService mSpotifyService;

  private ArtistsPresenter mArtistsPresenter;

  @Before public void setUpArtistsPresenter() {

    MockitoAnnotations.initMocks(this);

    // Mock and simulate the retrofit service we don't call the API
    SpotifyApp application = (SpotifyApp) RuntimeEnvironment.application;
    application.setSpotifyService(mSpotifyService);
    application.setScheduler(Schedulers.immediate());

    mArtistsPresenter = new ArtistsPresenter();
    when(mArtistsMvpView.getContext()).thenReturn(application);
    mArtistsPresenter.setView(mArtistsMvpView);
  }

  @Test public void shouldReturnArtists() {

    //we simulate a request to bring the artists
    final String artist = "muse";
    ArtistsSearch artistsSearch = FakeSpotifyAPI.getArtistSearch();
    when(mSpotifyService.searchArtist(artist)).thenReturn(Observable.just(artistsSearch));
    List<Artist> artists = artistsSearch.getArtists();

    //if the answer was successful then we validate the correct behavior of the presenter
    mArtistsPresenter.onResponse(artists);
    verify(mArtistsMvpView).hideLoading();
    verify(mArtistsMvpView).renderArtists(artists);
  }

  @Test public void shouldReturnNotFoundArtists() {
    //we simulate a request to bring the artists
    final String artist = "msdfsfuse123adreddga";
    ArtistsSearch artistsSearch = FakeSpotifyAPI.getArtistSearchEmpty();
    when(mSpotifyService.searchArtist(artist)).thenReturn(Observable.just(artistsSearch));
    List<Artist> artists = Collections.emptyList();

    //print this value if is "0" you should show NotFoundMessage
    artists.size();

    //This is a mistake because we are confirming on the
    // Interactor the responses of api calls and should be the
    // presenter who validate the use case but it is only one example.

    //if the answer was artists not found
    mArtistsPresenter.onArtistNotFound();
    verify(mArtistsMvpView).showArtistNotFoundMessage();
  }

  @Test public void shouldReturnNetworkConnectionError() {
    //we simulate a request to bring the artists not NetworkConnectionError
    final String artist = "muse";
    when(mSpotifyService.searchArtist(artist)).thenReturn(
        Observable.<ArtistsSearch>error(new RuntimeException("Error not connection")));

    //if the answer was Network Connection
    mArtistsPresenter.onNetworkConnectionError();
    verify(mArtistsMvpView).showConnectionErrorMessage();
  }

  @After public void detach() {
    mArtistsPresenter.detachView();
  }
}
