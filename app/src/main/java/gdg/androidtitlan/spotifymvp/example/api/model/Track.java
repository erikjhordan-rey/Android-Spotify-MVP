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

package gdg.androidtitlan.spotifymvp.example.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Track implements Parcelable {

  public static final Creator<Track> CREATOR = new Creator<Track>() {

    public Track createFromParcel(Parcel source) {
      return new Track(source);
    }

    public Track[] newArray(int size) {
      return new Track[size];
    }
  };

  @SerializedName("name") public String name;
  @SerializedName("preview_url") public String preview_url;
  @SerializedName("track_number") public int track_number;
  @SerializedName("album") public Album album;

  protected Track(Parcel in) {
    this.name = in.readString();
    this.preview_url = in.readString();
    this.track_number = in.readInt();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.name);
    parcel.writeString(this.preview_url);
    parcel.writeInt(this.track_number);
  }
}
