package com.mattfritz.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackPlayerFragment extends DialogFragment {

    private final String SHOW_DIALOG_TAG = "SHOW_DIALOG";
    private final String TRACKS_PLAYLIST_TAG = "TRACKS_PLAYLIST";

    public static TrackPlayerFragment newInstance() {
        return new TrackPlayerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            setShowsDialog(args.getBoolean(SHOW_DIALOG_TAG));
        } else {
            setShowsDialog(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_player, container, false);

        Bundle args = getArguments();
        Intent intent = getActivity().getIntent();
        ArrayList<Track> tracks = null;

        if (intent.hasExtra(TRACKS_PLAYLIST_TAG)) {
            tracks = intent.getParcelableArrayListExtra(TRACKS_PLAYLIST_TAG);
        } else {
            tracks = args.getParcelableArrayList(TRACKS_PLAYLIST_TAG);
        }

        if (tracks != null) {
            Track track = tracks.get(0);

            TextView artistNameTextView = (TextView) rootView.findViewById(R.id.player_artist_name_textview);
            artistNameTextView.setText(track.artistName);

            TextView albumNameTextView = (TextView) rootView.findViewById(R.id.player_album_name_textview);
            albumNameTextView.setText(track.albumName);

            ImageView albumArtImageView = (ImageView) rootView.findViewById(R.id.player_album_art_imageview);
            Picasso.with(getActivity())
                    .load(track.albumImageUrl)
                    .resize(200, 200)
                    .centerCrop()
                    .placeholder(R.drawable.ic_help_black_24dp)
                    .error(R.drawable.ic_help_black_24dp)
                    .into(albumArtImageView);

            TextView trackNameTextView = (TextView) rootView.findViewById(R.id.player_track_name_textview);
            trackNameTextView.setText(track.trackName);
        }

        return rootView;
    }
}
