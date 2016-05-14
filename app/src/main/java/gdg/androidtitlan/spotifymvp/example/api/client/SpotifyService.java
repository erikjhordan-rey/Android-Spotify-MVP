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

package gdg.androidtitlan.spotifymvp.example.api.client;

import gdg.androidtitlan.spotifymvp.example.api.model.ArtistsSearch;
import gdg.androidtitlan.spotifymvp.example.api.model.Tracks;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SpotifyService {

  @GET(Constants.ARTIST_SEARCH) Observable<ArtistsSearch> searchArtist(
      @Query(Constants.QUERY_SEARCH) String artist);

  @GET(Constants.ARTIST_TRACKS) Observable<Tracks> searchTrackList(
      @Path(Constants.PATH_ARTIST_TRACKS) String artistId);
}
