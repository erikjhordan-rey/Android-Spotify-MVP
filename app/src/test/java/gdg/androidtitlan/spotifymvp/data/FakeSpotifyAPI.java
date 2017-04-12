/**
 * Copyright 2016 Erik Jhordan Rey.
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

package gdg.androidtitlan.spotifymvp.data;

import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeSpotifyAPI {

  private static String ARTIST_ID_TEST = "12Chz98pHFMPJEknJQMWvI";
  private static String ARTIST_NAME_TEST = "muse";

  private FakeSpotifyAPI() {
  }

  public static List<Artist> getArtists() {
    List<Artist> artistsList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      artistsList.add(getArtist());
    }

    return artistsList;
  }

  public static Artist getArtist() {
    Artist artist = new Artist();
    artist.id = ARTIST_ID_TEST;
    artist.name = ARTIST_NAME_TEST;
    return artist;
  }

  public static ArtistsSearch getArtistSearch() {
    ArtistsSearch search = new ArtistsSearch();
    Artists artist = new Artists();
    artist.setArtists(getArtists());
    search.artistsSearch = artist;
    return search;
  }

  public static ArtistsSearch getArtistSearchEmpty() {
    ArtistsSearch search = new ArtistsSearch();
    Artists artist = new Artists();
    artist.setArtists(Collections.emptyList());
    search.artistsSearch = artist;
    return search;
  }
}
