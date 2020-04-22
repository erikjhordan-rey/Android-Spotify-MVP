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

package io.github.erikjhordanrey.mvp.presenter;

import android.content.ServiceConnection;
import io.github.erikjhordanrey.mvp.interactor.AudioFinishedListener;
import io.github.erikjhordanrey.mvp.interactor.PlayerInteractor;
import io.github.erikjhordanrey.mvp.view.service.AudioPlayerService;

public class AudioPlayerPresenter extends Presenter<AudioPlayerPresenter.View>
        implements AudioFinishedListener {

    private final PlayerInteractor playerInteractor;
    private ServiceConnection serviceConnection;

    public AudioPlayerPresenter(PlayerInteractor playerInteractor) {
        this.playerInteractor = playerInteractor;
        this.playerInteractor.setAudioFinishedListener(this);
    }

    public void onPreviewTrack() {
        playerInteractor.onPreview();
    }

    public void onNextTrack() {
        playerInteractor.onNext();
    }

    public void onPlayPauseTrack() {
        playerInteractor.onPlayStop();
    }

    public void onStartAudioService(String trackUrl) {
        getView().onStartAudioService(trackUrl, serviceConnection);
    }

    public void setInfoMediaPlayer(int trackPosition) {
        getView().setInfoTrackPlayer(trackPosition);
    }

    @Override
    public void terminate() {
        super.terminate();
        playerInteractor.destroyAudioService();
        setView(null);
    }

    @Override
    public void onPlay() {
        getView().isPlay();
    }

    @Override
    public void onPause() {
        getView().isPause();
    }

    @Override
    public void onSetTimeStart(int trackCurrentPosition) {
        getView().setTimeStart(trackCurrentPosition);
    }

    @Override
    public void onSetTimeFinished(AudioPlayerService audioPlayerService) {
        getView().setTimeFinished(audioPlayerService);
    }

    @Override
    public void onResetTrackDuration() {
        getView().onResetTrackDuration();
    }

    @Override
    public void onSetInfoTrackPlayer(int trackPosition) {
        getView().setInfoTrackPlayer(trackPosition);
    }

    @Override
    public void onServiceConnection(ServiceConnection serviceConnection) {
        this.serviceConnection = serviceConnection;
    }

    public interface View extends Presenter.View {

        void onStartAudioService(String trackUrl, ServiceConnection serviceConnection);

        void setInfoTrackPlayer(int trackPosition);

        void isPlay();

        void isPause();

        void setTimeStart(int trackCurrentPosition);

        void setTimeFinished(AudioPlayerService audioPlayerService);

        void onResetTrackDuration();
    }
}
