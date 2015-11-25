package gdg.androidtitlan.spotifymvp.music.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jhordan on 20/11/15.
 */
public class Artist implements Parcelable {


    @SerializedName("followers") public Followers followers;
    @SerializedName("href") public String href;
    @SerializedName("id") public String id;
    @SerializedName("images") public List<ArtistImages> artistImages;
    @SerializedName("name") public String name;
    @SerializedName("popularity") public int popularity;

    public Artist(){}

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.href);
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeParcelable(this.followers, 0);
        parcel.writeInt(this.popularity);
        parcel.writeTypedList(this.artistImages);
    }

    protected Artist(Parcel in) {
        this.href= in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.followers = in.readParcelable(Followers.class.getClassLoader());
        this.popularity = in.readInt();

        if (this.artistImages == null) {
            this.artistImages = new ArrayList();
        }
        in.readTypedList(this.artistImages,ArtistImages.CREATOR);

    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {

        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };



}
