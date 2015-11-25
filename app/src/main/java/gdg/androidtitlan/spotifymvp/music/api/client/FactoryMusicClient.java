package gdg.androidtitlan.spotifymvp.music.api.client;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Jhordan on 20/11/15.
 */
public class FactoryMusicClient {

    public static MusicService create() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MusicConstants.SPOTIFY_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(MusicService.class);
    }
}
