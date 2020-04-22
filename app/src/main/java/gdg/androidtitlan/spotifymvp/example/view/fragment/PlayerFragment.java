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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import gdg.androidtitlan.spotifymvp.databinding.FragmentAudioPlayerBinding;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import gdg.androidtitlan.spotifymvp.example.interactor.PlayerInteractor;
import gdg.androidtitlan.spotifymvp.example.presenter.AudioPlayerPresenter;
import gdg.androidtitlan.spotifymvp.example.view.activity.TracksActivity;
import gdg.androidtitlan.spotifymvp.example.view.service.AudioPlayerService;
import gdg.androidtitlan.spotifymvp.example.view.utils.ServiceUtils;
import java.lang.reflect.Type;
import java.util.List;

public class PlayerFragment extends DialogFragment
        implements AudioPlayerPresenter.View, SeekBar.OnSeekBarChangeListener {

    private AudioPlayerService audioPlayerService;
    private boolean isPlayerPlaying = false;
    private boolean isPlayerPaused = false;
    private int trackCurrentPosition;

    private List<Track> trackList;
    private int trackPosition;
    private AudioPlayerPresenter audioPlayerPresenter;

    public static PlayerFragment newInstance(String tracks, int position) {
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TracksActivity.EXTRA_TRACKS, tracks);
        bundle.putInt(TracksActivity.EXTRA_TRACK_POSITION, position);
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private FragmentAudioPlayerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trackList = getTrackList(getArguments().getString(TracksActivity.EXTRA_TRACKS));
        trackPosition = getArguments().getInt(TracksActivity.EXTRA_TRACK_POSITION);
        setupPresenter();
        setupUi();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) getDialog().setDismissMessage(null);
        audioPlayerPresenter.terminate();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        audioPlayerPresenter.terminate();
        super.onDestroy();
    }

    private void setupPresenter() {
        audioPlayerPresenter = new AudioPlayerPresenter(new PlayerInteractor(trackList, getContext()));
        audioPlayerPresenter.setView(this);
        audioPlayerPresenter.setInfoMediaPlayer(trackPosition);
        audioPlayerPresenter.onStartAudioService(trackList.get(trackPosition).preview_url);
    }

    private void setupUi() {
        binding.ibPreviewPlayer.setOnClickListener(v -> audioPlayerPresenter.onPreviewTrack());
        binding.ibNextPlayer.setOnClickListener(v -> audioPlayerPresenter.onNextTrack());
        binding.ibPlayPlayer.setOnClickListener(v -> audioPlayerPresenter.onPlayPauseTrack());
    }

    @Override
    public void setInfoTrackPlayer(int trackPosition) {
        binding.txtTrackTitlePlayer.setText(trackList.get(trackPosition).name);
        binding.txtAlbumTitlePlayer.setText(trackList.get(trackPosition).album.albumName);

        if (trackList.get(trackPosition).album.trackImages.size() > 0) {
            for (int i = 0; i < trackList.get(trackPosition).album.trackImages.size(); i++) {
                if (trackList.get(i) != null && trackList.get(trackPosition).album.trackImages.size() > 0) {
                    Picasso.with(getActivity())
                            .load(trackList.get(trackPosition).album.trackImages.get(0).url)
                            .into(binding.ivAlbumPlayer);
                }
            }
        } else {
            Picasso.with(getActivity())
                    .load("http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png")
                    .into(binding.ivAlbumPlayer);
        }
    }

    private List<Track> getTrackList(String tracks) {
        Gson gson = new GsonBuilder().create();
        Type trackType = new TypeToken<List<Track>>() {
        }.getType();
        return gson.fromJson(tracks, trackType);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        binding.txtTimeStart.setText("00:" + String.format("%02d", i));
        trackCurrentPosition = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (isPlayerPlaying) {
            audioPlayerService.noUpdateUI();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        trackCurrentPosition = seekBar.getProgress();
        if (audioPlayerService != null) {
            audioPlayerService.toSeekTrack(trackCurrentPosition, isPlayerPaused);
        }
    }

    @Override
    public void onStartAudioService(String trackUrl, ServiceConnection serviceConnection) {

        Intent serviceIntent = new Intent(getActivity(), AudioPlayerService.class);
        serviceIntent.putExtra(AudioPlayerService.EXTRA_TRACK_PREVIEW_URL, trackUrl);

        if (ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService.class, requireActivity())
                && !isPlayerPlaying) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().stopService(serviceIntent);
            getActivity().getApplicationContext().startService(serviceIntent);
        } else if (!ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService.class, requireActivity())) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().startService(serviceIntent);
        }
        if (audioPlayerService == null) {
            getActivity().getApplicationContext()
                    .bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void isPlay() {
        binding.ibPlayPlayer.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void isPause() {
        binding.ibPlayPlayer.setImageResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public void setTimeStart(int trackCurrentPosition) {
        binding.sbTimeProgressPlayer.setProgress(trackCurrentPosition);
        binding.txtTimeStart.setText("00:" + String.format("%02d", trackCurrentPosition));
    }

    @Override
    public void setTimeFinished(AudioPlayerService audioPlayerService) {
        binding.sbTimeProgressPlayer.setMax(audioPlayerService.getTrackDuration());
        binding.txtTimeEnd.setText(audioPlayerService.getTrackDurationString());
    }

    @Override
    public void onResetTrackDuration() {
        binding.sbTimeProgressPlayer.setProgress(0);
        binding.txtTimeStart.setText("");
        binding.txtTimeEnd.setText("");
    }

    @Override
    public Context context() {
        return getActivity();
    }
}
