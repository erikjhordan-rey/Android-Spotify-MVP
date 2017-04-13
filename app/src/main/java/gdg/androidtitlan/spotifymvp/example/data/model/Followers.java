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

package gdg.androidtitlan.spotifymvp.example.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import static gdg.androidtitlan.spotifymvp.example.data.api.Constants.Serialized.HREF;
import static gdg.androidtitlan.spotifymvp.example.data.api.Constants.Serialized.TOTAL;

public class Followers implements Parcelable {

  public static final Creator<Followers> CREATOR = new Creator<Followers>() {

    public Followers createFromParcel(Parcel source) {
      return new Followers(source);
    }

    public Followers[] newArray(int size) {
      return new Followers[size];
    }
  };

  @SerializedName(HREF) private String href;
  @SerializedName(TOTAL) public int totalFollowers;

  private Followers(Parcel in) {
    this.href = in.readString();
    this.totalFollowers = in.readInt();
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.href);
    parcel.writeInt(this.totalFollowers);
  }

  @Override public int describeContents() {
    return 0;
  }
}
