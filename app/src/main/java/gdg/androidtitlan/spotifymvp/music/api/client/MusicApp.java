package gdg.androidtitlan.spotifymvp.music.api.client;

import android.app.Application;
import android.content.Context;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Jhordan on 20/11/15.
 */
public class MusicApp extends Application {

    private MusicService musicService;
    private Scheduler defaultSubscribeScheduler;

    public static MusicApp get(Context context) {
        return (MusicApp) context.getApplicationContext();
    }

    public MusicService getMusicService() {
        if (musicService == null)
            musicService = FactoryMusicClient.create();

        return musicService;
    }

    //For setting mocks during testing
    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null)
            defaultSubscribeScheduler = Schedulers.io();

        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
