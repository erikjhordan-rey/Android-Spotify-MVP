package gdg.androidtitlan.spotifymvp.example.model;

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

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import gdg.androidtitlan.spotifymvp.example.api.model.Track;
import gdg.androidtitlan.spotifymvp.example.service.AudioPlayerService;
import java.util.List;

public class AudioPlayerInteractor {

  private AudioFinishedListener audioFinishedListener;
  private AudioPlayerService audioPlayerService;
  private boolean isServiceBounded;
  private boolean isPlayerPlaying = false;
  private boolean isPlayerPaused = false;
  private int trackDuration = 0;
  private int trackCurrentPosition;
  @SuppressLint("HandlerLeak") private final Handler playerHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (trackDuration == 0) {
        setTrackDuration();
      }
      trackCurrentPosition = msg.getData().getInt(AudioPlayerService.EXTRA_CURRENT_TRACK_POSITION);
      audioFinishedListener.onSetTimeStart(trackCurrentPosition);

      if (trackCurrentPosition == trackDuration && trackCurrentPosition != 0) {
        isPlayerPlaying = false;
        isPlayerPaused = false;
        trackCurrentPosition = 0;
      }
      if (isPlayerPlaying) {
        audioFinishedListener.onPause();
      } else {
        audioFinishedListener.onPlay();
      }
    }
  };
  private Context context;
  private List<Track> trackList;
  private int trackPosition;
  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      AudioPlayerService.PlayerBinder playerBinder = (AudioPlayerService.PlayerBinder) iBinder;
      audioPlayerService = playerBinder.getService();
      isServiceBounded = true;
      if (!isPlayerPlaying) {
        isPlayerPlaying = true;
      }
      setTrackDuration();
      audioPlayerService.setAudioPlayerHandler(playerHandler);
    }

    @Override public void onServiceDisconnected(ComponentName componentName) {
      isServiceBounded = false;
    }
  };

  public AudioPlayerInteractor(List<Track> trackList, Context context,
      AudioFinishedListener audioFinishedListener) {
    this.trackList = trackList;
    this.context = context;
    this.audioFinishedListener = audioFinishedListener;

    audioFinishedListener.onServiceConnection(serviceConnection);
  }

  public void setTrackDuration() {
    if (audioPlayerService != null) {
      trackDuration = audioPlayerService.getTrackDuration();
      audioFinishedListener.onSetTimeFinished(audioPlayerService);
    }
  }

  private void changeTrack(int trackPosition) {
    isPlayerPlaying = true;
    isPlayerPaused = false;
    audioFinishedListener.onSetInfoTrackPlayer(trackPosition);
    audioPlayerService.setTrackPreviewUrl(trackList.get(trackPosition).preview_url);
    audioPlayerService.noUpdateUI();
    audioPlayerService.onPlayAudio(0);
    audioFinishedListener.onResetTrackDuration();
    trackDuration = 0;
  }

  public void destroyAudioService() {

    if (audioPlayerService != null) {
      audioPlayerService.noUpdateUI();
      if (isServiceBounded) {
        context.getApplicationContext().unbindService(serviceConnection);
        isServiceBounded = false;
      }
    }
    if (!isPlayerPaused && !isPlayerPlaying) {
      context.getApplicationContext().stopService(new Intent(context, AudioPlayerService.class));
    }
  }

  public void onPreview() {
    trackPosition = trackPosition - 1;
    if (trackPosition < 0) {
      trackPosition = trackList.size() - 1;
    }
    changeTrack(trackPosition);
  }

  public void onNext() {
    trackPosition = (trackPosition + 1) % trackList.size();
    changeTrack(trackPosition);
  }

  public void onPlayStop() {

    if (isPlayerPlaying) {
      audioFinishedListener.onPlay();
      audioPlayerService.onPauseAudio();
      isPlayerPaused = true;
      isPlayerPlaying = false;
    } else {
      audioFinishedListener.onPause();
      audioPlayerService.onPlayAudio(trackCurrentPosition);
      isPlayerPaused = true;
      isPlayerPlaying = true;
    }
  }
}
