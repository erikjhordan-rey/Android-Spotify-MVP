package gdg.androidtitlan.spotifymvp.music.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.List;

import gdg.androidtitlan.spotifymvp.music.api.model.Track;
import gdg.androidtitlan.spotifymvp.music.service.AudioPlayerService;

/**
 * Created by Jhordan on 25/11/15.
 */
public class AudioPlayerInteractor {

    private AudioFinishedListener audioFinishedListener;
    private AudioPlayerService audioPlayerService;
    private boolean isServiceBounded;
    private boolean isPlayerPlaying = false;
    private boolean isPlayerPaused = false;
    private int trackDuration = 0;
    private int trackCurrentPosition;
    private Context context;
    private List<Track> trackList;
    private int trackPosition;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioPlayerService.PlayerBinder playerBinder = (AudioPlayerService.PlayerBinder) iBinder;
            audioPlayerService = playerBinder.getService();
            isServiceBounded = true;
            if (!isPlayerPlaying) {
                isPlayerPlaying = true;
            }
            setTrackDuration();
            audioPlayerService.setAudioPlayerHandler(playerHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBounded = false;
        }
    };

    private final Handler playerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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

    public AudioPlayerInteractor(List<Track> trackList, Context context, AudioFinishedListener audioFinishedListener) {
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
