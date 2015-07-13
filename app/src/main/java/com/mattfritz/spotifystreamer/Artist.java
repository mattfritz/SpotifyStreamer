package com.mattfritz.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
    String name;
    String imageUrl;
    String id;

    public Artist(String name, String imageUrl, String id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    private Artist(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        id = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(imageUrl);
        out.writeString(id);
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
