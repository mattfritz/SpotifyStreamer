package com.mattfritz.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class TrackPlayerFragment extends DialogFragment {

    private final String LOG_TAG = TrackPlayerFragment.class.getSimpleName();
    private final String SHOW_DIALOG_TAG = "SHOW_DIALOG";
    private final String PLAYLIST_TRACKS_TAG = "PLAYLIST_TRACKS";
    private final String PLAYLIST_POSITION_TAG = "PLAYLIST_POSITION";

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
        ArrayList<Track> tracks;
        int trackIndex;

        if (intent.hasExtra(PLAYLIST_TRACKS_TAG)) {
            tracks = intent.getParcelableArrayListExtra(PLAYLIST_TRACKS_TAG);
            trackIndex = intent.getIntExtra(PLAYLIST_POSITION_TAG, 0);
        } else {
            tracks = args.getParcelableArrayList(PLAYLIST_TRACKS_TAG);
            trackIndex = args.getInt(PLAYLIST_POSITION_TAG, 0);
        }

        if (tracks != null) {
            Track track = tracks.get(trackIndex);

            // TODO: refactor to use viewholder pattern
            // Load view with artist, album, and track information
            TextView artistNameTextView = (TextView) rootView.findViewById(R.id.player_artist_name_textview);
            TextView albumNameTextView = (TextView) rootView.findViewById(R.id.player_album_name_textview);
            ImageView albumArtImageView = (ImageView) rootView.findViewById(R.id.player_album_art_imageview);
            TextView trackNameTextView = (TextView) rootView.findViewById(R.id.player_track_name_textview);
            final ImageButton playButton = (ImageButton) rootView.findViewById(R.id.player_play_button);
            ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.player_previous_button);
            ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.player_next_button);

            artistNameTextView.setText(track.artistName);

            albumNameTextView.setText(track.albumName);

            Picasso.with(getActivity())
                    .load(track.albumImageUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_help_black_24dp)
                    .error(R.drawable.ic_help_black_24dp)
                    .into(albumArtImageView);

            trackNameTextView.setText(track.trackName);

            String audioUrl = track.previewUrl;
            final MediaPlayer mp = new MediaPlayer();

            // Autoplay selected track
            try {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setDataSource(audioUrl);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                // TODO: Handle this properly with user feedback
                Log.e(LOG_TAG, "Error streaming audio");
                e.printStackTrace();
            }

            // Event listeners for audio controls
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        String uri = "android:drawable/ic_media_play";
                        int image = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        playButton.setImageResource(image);

                        mp.pause();
                    } else {
                        String uri = "android:drawable/ic_media_pause";
                        int image = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        playButton.setImageResource(image);

                        mp.start();
                    }
                }
            });

            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: actually do something with this when tracks are dynamically selected
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: actually do something with this when tracks are dynamically selected
                }
            });

        }

        return rootView;
    }
}
