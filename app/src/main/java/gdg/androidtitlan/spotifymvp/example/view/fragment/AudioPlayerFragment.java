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

package gdg.androidtitlan.spotifymvp.example.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import gdg.androidtitlan.spotifymvp.R;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import gdg.androidtitlan.spotifymvp.example.presenter.AudioPlayerPresenter;
import gdg.androidtitlan.spotifymvp.example.view.service.AudioPlayerService;
import gdg.androidtitlan.spotifymvp.example.view.activity.TracksActivity;
import gdg.androidtitlan.spotifymvp.example.view.utils.ServiceUtils;
import gdg.androidtitlan.spotifymvp.example.presenter.PlayerMVPView;
import java.lang.reflect.Type;
import java.util.List;

public class AudioPlayerFragment extends DialogFragment
    implements PlayerMVPView, SeekBar.OnSeekBarChangeListener {

  @BindView(R.id.iv_album_player) ImageView iv_album_player;
  @BindView(R.id.txt_track_title_player) TextView txt_track_title_player;
  @BindView(R.id.txt_album_title_player) TextView txt_album_title_player;
  @BindView(R.id.sb_time_progress_player) SeekBar sb_time_progress_player;
  @BindView(R.id.txt_time_start) TextView txt_time_start;
  @BindView(R.id.txt_time_end) TextView txt_time_end;
  @BindView(R.id.ib_play_player) ImageButton ib_play_player;

  private AudioPlayerService audioPlayerService;
  private boolean isPlayerPlaying = false;
  private boolean isPlayerPaused = false;
  private int trackCurrentPosition;

  private List<Track> trackList;
  private int trackPosition;
  private AudioPlayerPresenter audioPlayerPresenter;

  public static AudioPlayerFragment newInstance(String tracks, int position) {
    AudioPlayerFragment audioPlayerFragment = new AudioPlayerFragment();
    Bundle bundle = new Bundle();
    bundle.putString(TracksActivity.EXTRA_TRACKS, tracks);
    bundle.putInt(TracksActivity.EXTRA_TRACK_POSITION, position);
    audioPlayerFragment.setArguments(bundle);
    return audioPlayerFragment;
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    return dialog;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_audio_player, container, false);
    ButterKnife.bind(this, rootView);

    trackList = getTrackList(getArguments().getString(TracksActivity.EXTRA_TRACKS));
    trackPosition = getArguments().getInt(TracksActivity.EXTRA_TRACK_POSITION);
    audioPlayerPresenter = new AudioPlayerPresenter(trackList);
    audioPlayerPresenter.setView(this);

    audioPlayerPresenter.setInfoMediaPlayer(trackPosition);
    audioPlayerPresenter.onStartAudioService(trackList.get(trackPosition).preview_url);

    return rootView;
  }

  @Override public void onDestroyView() {
    if (getDialog() != null && getRetainInstance()) {
      getDialog().setDismissMessage(null);
    }
    audioPlayerPresenter.detachView();
    super.onDestroyView();
  }

  @OnClick(R.id.ib_preview_player) public void previewTrack() {
    audioPlayerPresenter.onPreviewTrack();
  }

  @OnClick(R.id.ib_next_player) public void nextTrack() {
    audioPlayerPresenter.onNextTrack();
  }

  @OnClick(R.id.ib_play_player) public void playTrack() {
    audioPlayerPresenter.onPlayPauseTrack();
  }

  @Override public void setInfoTrackPlayer(int trackPosition) {

    txt_track_title_player.setText(trackList.get(trackPosition).name);
    txt_album_title_player.setText(trackList.get(trackPosition).album.albumName);

    if (trackList.get(trackPosition).album.trackImages.size() > 0) {
      for (int i = 0; i < trackList.get(trackPosition).album.trackImages.size(); i++) {
        if (trackList.get(i) != null && trackList.get(trackPosition).album.trackImages.size() > 0) {
          Picasso.with(getActivity())
              .load(trackList.get(trackPosition).album.trackImages.get(0).url)
              .into(iv_album_player);
        }
      }
    } else {
      Picasso.with(getActivity())
          .load("http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png")
          .into(iv_album_player);
    }
  }

  @Override public void onDestroy() {
    audioPlayerPresenter.detachView();
    super.onDestroy();
  }

  private List<Track> getTrackList(String tracks) {
    Gson gson = new GsonBuilder().create();
    Type trackType = new TypeToken<List<Track>>() {
    }.getType();
    return gson.fromJson(tracks, trackType);
  }

  @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    txt_time_start.setText("00:" + String.format("%02d", i));
    trackCurrentPosition = i;
  }

  @Override public void onStartTrackingTouch(SeekBar seekBar) {
    if (isPlayerPlaying) {
      audioPlayerService.noUpdateUI();
    }
  }

  @Override public void onStopTrackingTouch(SeekBar seekBar) {
    trackCurrentPosition = seekBar.getProgress();
    if (audioPlayerService != null) {
      audioPlayerService.toSeekTrack(trackCurrentPosition, isPlayerPaused);
    }
  }

  @Override public void onStartAudioService(String trackUrl, ServiceConnection serviceConnection) {

    Intent serviceIntent = new Intent(getActivity(), AudioPlayerService.class);
    serviceIntent.putExtra(AudioPlayerService.EXTRA_TRACK_PREVIEW_URL, trackUrl);

    if (ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService.class, getActivity())
        && !isPlayerPlaying) {
      trackCurrentPosition = 0;
      getActivity().getApplicationContext().stopService(serviceIntent);
      getActivity().getApplicationContext().startService(serviceIntent);
    } else if (!ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService.class, getActivity())) {
      trackCurrentPosition = 0;
      getActivity().getApplicationContext().startService(serviceIntent);
    }
    if (audioPlayerService == null) {
      getActivity().getApplicationContext()
          .bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
  }

  @Override public void isPlay() {
    ib_play_player.setImageResource(android.R.drawable.ic_media_play);
  }

  @Override public void isPause() {
    ib_play_player.setImageResource(android.R.drawable.ic_media_pause);
  }

  @Override public void setTimeStart(int trackCurrentPosition) {
    sb_time_progress_player.setProgress(trackCurrentPosition);
    txt_time_start.setText("00:" + String.format("%02d", trackCurrentPosition));
  }

  @Override public void setTimeFinished(AudioPlayerService audioPlayerService) {
    sb_time_progress_player.setMax(audioPlayerService.getTrackDuration());
    txt_time_end.setText(audioPlayerService.getTrackDurationString());
  }

  @Override public void onResetTrackDuration() {
    sb_time_progress_player.setProgress(0);
    txt_time_start.setText("");
    txt_time_end.setText("");
  }

  @Override public Context getContext() {
    return getActivity();
  }
}
