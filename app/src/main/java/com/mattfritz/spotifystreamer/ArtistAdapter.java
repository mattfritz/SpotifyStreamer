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

public class ArtistAdapter extends ArrayAdapter<com.mattfritz.spotifystreamer.Artist> {

    private static final String LOG_TAG = ArtistAdapter.class.getSimpleName();

    public ArtistAdapter(Activity context, List<com.mattfritz.spotifystreamer.Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.mattfritz.spotifystreamer.Artist artist = getItem(position);
        String imageUrl = null;
        Context context = getContext();

        // Only fetch an image URL if Spotify returns at least one
        if (artist.imageUrl != null) {
            imageUrl = artist.imageUrl;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_artist, parent, false);
        }

        ImageView artistImageView = (ImageView) convertView.findViewById(R.id.list_item_artist_icon);
        Picasso.with(context)
                .load(imageUrl)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.ic_help_black_24dp)
                .error(R.drawable.ic_help_black_24dp)
                .into(artistImageView);

        TextView artistNameView = (TextView) convertView.findViewById(R.id.list_item_artist_name);
        artistNameView.setText(artist.name);

        return convertView;
    }
}
