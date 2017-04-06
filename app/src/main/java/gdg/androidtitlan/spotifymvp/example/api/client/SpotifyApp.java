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

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class SpotifyApp extends Application {

  private SpotifyService mSpotifyService;
  private Scheduler mScheduler;

  public static SpotifyApp get(Context context) {
    return (SpotifyApp) context.getApplicationContext();
  }

  public SpotifyService getSpotifyService() {
    if (mSpotifyService == null) {
      mSpotifyService = FactorySpotifyClient.create();
    }

    return mSpotifyService;
  }

  public Scheduler subscribeScheduler() {
    if (mScheduler == null) {
      mScheduler = Schedulers.io();
    }

    return mScheduler;
  }

  @VisibleForTesting public void setSpotifyService(SpotifyService spotifyService) {
    this.mSpotifyService = spotifyService;
  }

  @VisibleForTesting public void setScheduler(Scheduler scheduler) {
    this.mScheduler = scheduler;
  }
}
