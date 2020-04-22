/**
 * Copyright 2015 Erik Jhordan Rey.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gdg.androidtitlan.spotifymvp.example.data.api.retrofit;

import gdg.androidtitlan.spotifymvp.example.data.api.Constants;
import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyRetrofitService {

    @GET(Constants.Endpoint.ARTIST_SEARCH)
    Observable<List<Artist>> searchArtist(@Query(Constants.Params.QUERY_SEARCH) String artist);

    @GET(Constants.Endpoint.ARTIST_TRACKS)
    Observable<List<Track>> getTracks(@Path(Constants.Params.ARTIST_ID) String artistId);
}
