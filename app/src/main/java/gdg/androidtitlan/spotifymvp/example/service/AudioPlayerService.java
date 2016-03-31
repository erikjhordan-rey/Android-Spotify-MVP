package gdg.androidtitlan.spotifymvp.example.service;

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

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayerService extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener {

  public static final String EXTRA_IS_PLAYER = "is_player";
  public static final String EXTRA_CURRENT_TRACK_POSITION = "current_track_position";
  public static final String EXTRA_TRACK_PREVIEW_URL = "track_preview_url";

  private PlayerBinder mediaPlayerBinder = null;
  private Handler mediaPlayerHandler;
  private Timer timer;
  private int currentTrackPosition;
  private boolean isPlayerPaused;
  private MediaPlayer mediaPlayer = null;
  private String trackPreviewUrl;

  /**
   * This method is executed first (1er)
   * initialize the Binder.
   **/

  @Override public void onCreate() {
    super.onCreate();
    mediaPlayerBinder = new PlayerBinder();
  }

  /**
   * This method is executed second (2nd)
   * Receives a string (EXTRA_TRACK_PREVIEW_URL) of intent from where he was released
   **/

  @Override public int onStartCommand(Intent intent, int flags, int startId) {

    if (intent != null && intent.hasExtra(EXTRA_TRACK_PREVIEW_URL)) {
      if (intent.getStringExtra(EXTRA_TRACK_PREVIEW_URL) != null) {
        setTrackPreviewUrl(intent.getStringExtra(EXTRA_TRACK_PREVIEW_URL));
        onPlayAudio(0);
      }
    }
    return START_STICKY;
  }

  /**
   * This method is executed third (3th)
   * returns an IBinder object that defines the programming
   * interface that clients can use to interact with the service
   **/

  @Nullable @Override public IBinder onBind(Intent intent) {
    return mediaPlayerBinder;
  }


    /*
     * This method is executed fourth (4th)
     * This method is called when the media file is ready for playback.
     */

  @Override public void onPrepared(MediaPlayer mediaPlayer) {
    mediaPlayer.start();
    if (currentTrackPosition != 0) mediaPlayer.seekTo(currentTrackPosition * 1000);

    updateUI();
  }


    /*
     * This method is executed fiveth (5th)
     * This method is called when the media file is finished playback
     */

  @Override public void onCompletion(MediaPlayer mediaPlayer) {

    Message completionMessage = new Message();
    Bundle completionBundle = new Bundle();
    completionBundle.putBoolean(EXTRA_IS_PLAYER, false);
    completionMessage.setData(completionBundle);
    if (mediaPlayerHandler != null) mediaPlayerHandler.sendMessage(completionMessage);

    noUpdateUI();
  }

  @Override public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
    return false;
  }

  /**
   * @param trackPreviewUrl
   */
  public void setTrackPreviewUrl(String trackPreviewUrl) {
    this.trackPreviewUrl = trackPreviewUrl;
  }


    /*
     * We validate the state of the media player if isPlaying() to play the track
     */

  public void onPlayAudio(int trackPosition) {
    currentTrackPosition = trackPosition;
    if (mediaPlayer != null) {
      if (mediaPlayer.isPlaying()) mediaPlayer.stop();

      mediaPlayer.reset();
    }
    setupAudioPlayer();
    isPlayerPaused = false;
  }

  public int onPauseAudio() {
    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
      isPlayerPaused = true;
      noUpdateUI();
      return mediaPlayer.getDuration() / 1000;
    } else {
      return 0;
    }
  }


    /*
     * Initialize the track and call OnPreparedListener and OnCompletionListener.
     */

  public void setupAudioPlayer() {

    if (mediaPlayer == null) mediaPlayer = new MediaPlayer();

    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    try {
      mediaPlayer.setDataSource(trackPreviewUrl);
      mediaPlayer.prepareAsync();
      mediaPlayer.setOnCompletionListener(AudioPlayerService.this);
      mediaPlayer.setOnPreparedListener(AudioPlayerService.this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    mediaPlayer.setOnErrorListener(AudioPlayerService.this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (timer != null) {
      noUpdateUI();
    }
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
    if (mediaPlayerHandler != null) {
      mediaPlayerHandler = null;
    }
  }

  public void noUpdateUI() {
    if (timer != null) {
      timer.cancel();
      timer.purge();
    }
  }

  public void updateUI() {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override public void run() {
        sendCurrentTrackPosition();
      }
    }, 0, 1000);
  }

  private void sendCurrentTrackPosition() {
    Message message = new Message();
    message.setData(getCurrentTrackPosition());
    if (mediaPlayerHandler != null) mediaPlayerHandler.sendMessage(message);
  }

  private Bundle getCurrentTrackPosition() {
    Bundle uiBundle = new Bundle();
    if (mediaPlayer != null && (isPlayerPaused || mediaPlayer.isPlaying())) {
      uiBundle.putBoolean(EXTRA_IS_PLAYER, true);
      int trackPosition = (int) Math.ceil((double) mediaPlayer.getCurrentPosition() / 1000);
      uiBundle.putInt(EXTRA_CURRENT_TRACK_POSITION, trackPosition);
    }
    return uiBundle;
  }

  public void toSeekTrack(int trackProgress, boolean isTrackPaused) {
    if ((mediaPlayer != null && isTrackPaused && !mediaPlayer.isPlaying()) || (mediaPlayer != null
        && mediaPlayer.isPlaying())) {
      mediaPlayer.seekTo(trackProgress * 1000);
      if (mediaPlayer.isPlaying()) {
        updateUI();
      }
    }
  }

  public String getTrackDurationString() {
    return "00:" + String.format("%02d", getTrackDuration());
  }

  public int getTrackDuration() {
    if (mediaPlayer != null && (isPlayerPaused || mediaPlayer.isPlaying())) {
      return (mediaPlayer.getDuration() / 1000);
    } else {
      return 0;
    }
  }

  public void setAudioPlayerHandler(Handler spotifyPlayerHandler) {

    this.mediaPlayerHandler = spotifyPlayerHandler;
    Message playerMessage = new Message();
    Bundle playerBundle;
    if (this.mediaPlayerHandler != null && (isPlayerPaused || mediaPlayer.isPlaying())) {
      playerBundle = getCurrentTrackPosition();

      if (!isPlayerPaused) {
        updateUI();
      } else {
        playerBundle.putBoolean(EXTRA_IS_PLAYER, false);
      }
      playerMessage.setData(playerBundle);
      if (this.mediaPlayerHandler != null) {
        this.mediaPlayerHandler.sendMessage(playerMessage);
      }
    }
  }

  public class PlayerBinder extends Binder {
    public AudioPlayerService getService() {
      return AudioPlayerService.this;
    }
  }
}
