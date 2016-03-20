/**
 * Copyright 2015 Erik Jhordan Rey.
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

package gdg.androidtitlan.spotifymvp.example.presenter;

import java.util.List;

import gdg.androidtitlan.spotifymvp.example.api.model.Track;
import gdg.androidtitlan.spotifymvp.example.model.TrackCallback;
import gdg.androidtitlan.spotifymvp.example.model.TracksInteractor;
import gdg.androidtitlan.spotifymvp.example.view.TracksMvpView;


public class TracksPresenter implements Presenter<TracksMvpView>, TrackCallback {

    private TracksMvpView tracksMvpView;
    private TracksInteractor tracksInteractor;

    public TracksPresenter() {
    }

    @Override
    public void setView(TracksMvpView view) {

        if (view == null)
            throw new IllegalArgumentException("You can't set a null view");

        tracksMvpView = view;
        tracksInteractor = new TracksInteractor(tracksMvpView.getContext());
    }

    @Override
    public void detachView() {
        tracksMvpView = null;
    }

    public void onSearchTracks(String string) {
        tracksMvpView.showLoading();
        tracksInteractor.loadData(string, this);
    }

    public void launchArtistDetail(List<Track> tracks, Track track, int position) {
        tracksMvpView.launchTrackDetail(tracks, track, position);
    }

    @Override
    public void onResponse(List<Track> tracks) {
        tracksMvpView.hideLoading();
        tracksMvpView.renderTracks(tracks);

    }

    @Override
    public void onTrackNotFound() {
        tracksMvpView.showTracksNotFoundMessage();
    }

    @Override
    public void onNetworkConnectionError() {
        tracksMvpView.showConnectionErrorMessage();
    }

    @Override
    public void onServerError() {
        tracksMvpView.showConnectionErrorMessage();
    }
}
