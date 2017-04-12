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

package gdg.androidtitlan.spotifymvp.example.data.api;

import static gdg.androidtitlan.spotifymvp.example.data.api.Constants.Params.ARTIST_ID;

public class Constants {

  public static final String SPOTIFY_API = "https://api.spotify.com";

  public static final class Endpoint {

    public static final String ARTIST_SEARCH = "/v1/search?type=artist";
    public static final String ARTIST_TRACKS =
        "v1/artists/{" + ARTIST_ID + "}/top-tracks?country=SE";
  }

  public static final class Params {
    public static final String QUERY_SEARCH = "q";
    public static final String ARTIST_ID = "artistId";
  }

  public static final class Serialized {

  }

  public static final class Deserializer {

    public static final String ARTISTS = "artists";
    public static final String ITEMS = "items";
    public static final String TRACKS = "tracks";
  }
}
