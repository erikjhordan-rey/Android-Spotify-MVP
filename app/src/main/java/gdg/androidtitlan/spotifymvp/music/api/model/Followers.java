package gdg.androidtitlan.spotifymvp.music.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 20/11/15.
 */
public class Followers implements Parcelable {

    @SerializedName("href") public String href;
    @SerializedName("total") public int totalFollowers;

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.href);
        parcel.writeInt(this.totalFollowers);
    }

    protected Followers(Parcel in) {
        this.href= in.readString();
        this.totalFollowers = in.readInt();
    }

    public static final Creator<Followers> CREATOR = new Creator<Followers>() {

        public Followers createFromParcel(Parcel source) {
            return new Followers(source);
        }

        public Followers[] newArray(int size) {
            return new Followers[size];
        }
    };


}
