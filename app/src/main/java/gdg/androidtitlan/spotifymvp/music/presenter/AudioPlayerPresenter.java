package gdg.androidtitlan.spotifymvp.music.presenter;

import android.content.ServiceConnection;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Track;
import gdg.androidtitlan.spotifymvp.music.model.AudioFinishedListener;
import gdg.androidtitlan.spotifymvp.music.model.AudioPlayerInteractor;
import gdg.androidtitlan.spotifymvp.music.service.AudioPlayerService;
import gdg.androidtitlan.spotifymvp.music.view.PlayerMVPView;

/**
 * Created by Jhordan on 25/11/15.
 */
public class AudioPlayerPresenter implements Presenter<PlayerMVPView>, AudioFinishedListener {

    private PlayerMVPView playerMVPView;
    private AudioPlayerInteractor audioPlayerInteractor;
    private List<Track> trackList;
    private ServiceConnection serviceConnection;

    public AudioPlayerPresenter(List<Track> trackList) {
        this.trackList = trackList;
    }

    @Override public void attachedView(PlayerMVPView view) {
        if (view == null)
            throw new IllegalArgumentException("You can't set a null view");

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
