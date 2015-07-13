package com.mattfritz.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class TrackAdapter extends ArrayAdapter<Track> {

    private static final String LOG_TAG = ArtistAdapter.class.getSimpleName();

    public TrackAdapter(Activity context, List<Track> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);
        String imageUrl = null;
        Context context = getContext();

        // Only fetch an image URL if Spotify returns at least one
        if (track.albumImageUrl != null) {
            imageUrl = track.albumImageUrl;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_track, parent, false);
        }

        ImageView albumImageView = (ImageView) convertView.findViewById(R.id.list_item_album_icon);
        Picasso.with(context)
                .load(imageUrl)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.ic_help_black_24dp)
                .error(R.drawable.ic_help_black_24dp)
                .into(albumImageView);

        TextView trackNameView = (TextView) convertView.findViewById(R.id.list_item_track_name);
        trackNameView.setText(track.trackName);

        TextView albumNameView = (TextView) convertView.findViewById(R.id.list_item_album_name);
        albumNameView.setText(track.albumName);

        return convertView;
    }
}
