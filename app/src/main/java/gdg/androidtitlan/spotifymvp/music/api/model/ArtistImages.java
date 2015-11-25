package gdg.androidtitlan.spotifymvp.music.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 20/11/15.
 */
public class ArtistImages implements Parcelable {

    @SerializedName("height") public int heigth;
    @SerializedName("url") public String url;
    @SerializedName("width") public int width;


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.heigth);
        parcel.writeString(this.url);
        parcel.writeInt(this.width);
    }

    protected ArtistImages(Parcel in){
        this.heigth = in.readInt();
        this.url = in.readString();
        this.width = in.readInt();
    }

    public static final Creator<ArtistImages> CREATOR = new Creator<ArtistImages>() {

        public ArtistImages createFromParcel(Parcel source) {
            return new ArtistImages(source);
        }

        public ArtistImages[] newArray(int size) {
            return new ArtistImages[size];
        }
    };

}
