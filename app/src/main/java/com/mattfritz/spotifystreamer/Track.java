package com.mattfritz.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    String trackName;
    String albumName;
    String albumImageUrl;
    String id;

    public Track(String trackName, String albumName, String albumImageUrl, String id) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.albumImageUrl = albumImageUrl;
        this.id = id;
    }

    private Track(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        albumImageUrl = in.readString();
        id = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(trackName);
        out.writeString(albumName);
        out.writeString(albumImageUrl);
        out.writeString(id);
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
