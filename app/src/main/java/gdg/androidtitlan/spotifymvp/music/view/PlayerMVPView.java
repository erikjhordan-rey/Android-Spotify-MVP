package gdg.androidtitlan.spotifymvp.music.view;

import android.content.ServiceConnection;

import gdg.androidtitlan.spotifymvp.music.service.AudioPlayerService;

/**
 * Created by Jhordan on 25/11/15.
 */
public interface PlayerMVPView extends MvpView {

    void onStartAudioService(String trackUrl,ServiceConnection serviceConnection);

    void setInfoTrackPlayer(int trackPosition);

    void isPlay();

    void isPause();

    void setTimeStart(int trackCurrentPosition);

    void setTimeFinished(AudioPlayerService audioPlayerService);

    void onResetTrackDuration();


}
