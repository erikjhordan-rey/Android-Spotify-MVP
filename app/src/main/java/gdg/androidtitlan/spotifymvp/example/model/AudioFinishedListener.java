package gdg.androidtitlan.spotifymvp.example.model;

import android.content.ServiceConnection;

import gdg.androidtitlan.spotifymvp.example.service.AudioPlayerService;

/**
 * Created by Jhordan on 25/11/15.
 */
public interface AudioFinishedListener {

  void onPlay();

  void onPause();

  void onSetTimeStart(int trackCurrentPosition);

  void onSetTimeFinished(AudioPlayerService audioPlayerService);

  void onResetTrackDuration();

  void onSetInfoTrackPlayer(int trackPosition);

  void onServiceConnection(ServiceConnection serviceConnection);
}
