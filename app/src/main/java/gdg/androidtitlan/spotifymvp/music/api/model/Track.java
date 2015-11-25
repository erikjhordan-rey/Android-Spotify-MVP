package gdg.androidtitlan.spotifymvp.music.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jhordan on 23/11/15.
 */
public class Track implements Parcelable {

    @SerializedName("name") public String name;
    @SerializedName("preview_url") public String preview_url;
    @SerializedName("track_number") public int track_number;
    @SerializedName("album") public Album album;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.preview_url);
        parcel.writeInt(this.track_number);

    }
    protected Track(Parcel in) {
        this.name= in.readString();
        this.preview_url = in.readString();
        this.track_number = in.readInt();

    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {

        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

}
