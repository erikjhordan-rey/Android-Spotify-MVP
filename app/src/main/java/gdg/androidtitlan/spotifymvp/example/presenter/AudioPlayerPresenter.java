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

import android.content.ServiceConnection;
import gdg.androidtitlan.spotifymvp.example.api.model.Track;
import gdg.androidtitlan.spotifymvp.example.model.AudioFinishedListener;
import gdg.androidtitlan.spotifymvp.example.model.AudioPlayerInteractor;
import gdg.androidtitlan.spotifymvp.example.service.AudioPlayerService;
import gdg.androidtitlan.spotifymvp.example.view.PlayerMVPView;
import java.util.List;

public class AudioPlayerPresenter implements Presenter<PlayerMVPView>, AudioFinishedListener {

  private PlayerMVPView playerMVPView;
  private AudioPlayerInteractor audioPlayerInteractor;
  private List<Track> trackList;
  private ServiceConnection serviceConnection;

  public AudioPlayerPresenter(List<Track> trackList) {
    this.trackList = trackList;
  }

  @Override public void setView(PlayerMVPView view) {
    if (view == null) {
      throw new IllegalArgumentException("You can't set a null view");
    }

    playerMVPView = view;
    audioPlayerInteractor = new AudioPlayerInteractor(trackList, playerMVPView.getContext(), this);
  }

  public void onPreviewTrack() {
    audioPlayerInteractor.onPreview();
  }

  public void onNextTrack() {
    audioPlayerInteractor.onNext();
  }

  public void onPlayPauseTrack() {
    audioPlayerInteractor.onPlayStop();
  }

  public void onStartAudioService(String trackUrl) {
    playerMVPView.onStartAudioService(trackUrl, serviceConnection);
  }

  public void setInfoMediaPlayer(int trackPosition) {
    playerMVPView.setInfoTrackPlayer(trackPosition);
  }

  @Override public void detachView() {
    audioPlayerInteractor.destroyAudioService();
    playerMVPView = null;
  }

  @Override public void onPlay() {
    playerMVPView.isPlay();
  }

  @Override public void onPause() {
    playerMVPView.isPause();
  }

  @Override public void onSetTimeStart(int trackCurrentPosition) {
    playerMVPView.setTimeStart(trackCurrentPosition);
  }

  @Override public void onSetTimeFinished(AudioPlayerService audioPlayerService) {
    playerMVPView.setTimeFinished(audioPlayerService);
  }

  @Override public void onResetTrackDuration() {
    playerMVPView.onResetTrackDuration();
  }

  @Override public void onSetInfoTrackPlayer(int trackPosition) {
    playerMVPView.setInfoTrackPlayer(trackPosition);
  }

  @Override public void onServiceConnection(ServiceConnection serviceConnection) {
    this.serviceConnection = serviceConnection;
  }
}
