package gdg.androidtitlan.spotifymvp.music.api.client;

import gdg.androidtitlan.spotifymvp.music.api.model.ArtistsSearch;
import gdg.androidtitlan.spotifymvp.music.api.model.Tracks;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Jhordan on 20/11/15.
 */
public interface MusicService {

    @GET(MusicConstants.ARTIST_SEARCH)
    Observable<ArtistsSearch> searchArtist(@Query(MusicConstants.QUERY_SEARCH)String artist);

    @GET(MusicConstants.ARTIST_TRACKS)
    Observable<Tracks> searchTrackList(@Path(MusicConstants.PATH_ARTIST_TRACKS)String artistId);
}
