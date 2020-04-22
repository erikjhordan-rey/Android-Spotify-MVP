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
 *
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
 *
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
 *
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
 *
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

package io.github.erikjhordanrey.mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

import static io.github.erikjhordanrey.mvp.data.api.Constants.Serialized.FOLLOWERS;
import static io.github.erikjhordanrey.mvp.data.api.Constants.Serialized.HREF;
import static io.github.erikjhordanrey.mvp.data.api.Constants.Serialized.ID;
import static io.github.erikjhordanrey.mvp.data.api.Constants.Serialized.IMAGES;
import static io.github.erikjhordanrey.mvp.data.api.Constants.Serialized.NAME;
import static io.github.erikjhordanrey.mvp.data.api.Constants.Serialized.POPULARITY;

public class Artist implements Parcelable {

  public static final Creator<Artist> CREATOR = new Creator<Artist>() {

    public Artist createFromParcel(Parcel source) {
      return new Artist(source);
    }

    public Artist[] newArray(int size) {
      return new Artist[size];
    }
  };

  @SerializedName(FOLLOWERS) public Followers followers;
  @SerializedName(HREF) private String href;
  @SerializedName(ID) public String id;
  @SerializedName(IMAGES) public List<ArtistImages> artistImages;
  @SerializedName(NAME) public String name;
  @SerializedName(POPULARITY) private int popularity;

  public Artist() {
  }

  protected Artist(Parcel in) {
    this.href = in.readString();
    this.id = in.readString();
    this.name = in.readString();
    this.followers = in.readParcelable(Followers.class.getClassLoader());
    this.popularity = in.readInt();

    if (this.artistImages == null) {
      this.artistImages = new ArrayList();
    }
    in.readTypedList(this.artistImages, ArtistImages.CREATOR);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.href);
    parcel.writeString(this.id);
    parcel.writeString(this.name);
    parcel.writeParcelable(this.followers, 0);
    parcel.writeInt(this.popularity);
    parcel.writeTypedList(this.artistImages);
  }
}
