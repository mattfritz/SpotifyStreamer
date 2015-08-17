package com.mattfritz.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    String trackName;
    String albumName;
    String albumImageUrl;
    String id;
    String previewUrl;
    String artistName;

    public Track(String trackName, String albumName, String albumImageUrl, String id, String previewUrl, String artistName) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.albumImageUrl = albumImageUrl;
        this.id = id;
        this.previewUrl = previewUrl;
        this.artistName = artistName;
    }

    private Track(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        albumImageUrl = in.readString();
        id = in.readString();
        previewUrl = in.readString();
        artistName = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(trackName);
        out.writeString(albumName);
        out.writeString(albumImageUrl);
        out.writeString(id);
        out.writeString(previewUrl);
        out.writeString(artistName);
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
