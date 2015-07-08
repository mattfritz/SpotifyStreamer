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

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistAdapter extends ArrayAdapter<Artist> {

    private static final String LOG_TAG = ArtistAdapter.class.getSimpleName();

    public ArtistAdapter(Activity context, List<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Artist artist = getItem(position);
        String imageUrl = null;
        Context context = getContext();

        // Only fetch an image URL if Spotify returns at least one
        if (artist.images.size() > 0) {
            imageUrl = artist.images.get(0).url;
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
