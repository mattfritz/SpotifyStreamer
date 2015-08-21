package com.mattfritz.spotifystreamer;

import android.content.Context;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class TrackPlayerFragment extends DialogFragment {

    private final String LOG_TAG = TrackPlayerFragment.class.getSimpleName();
    private final String SHOW_DIALOG_TAG = "SHOW_DIALOG";
    private final String PLAYLIST_TRACKS_TAG = "PLAYLIST_TRACKS";
    private final String PLAYLIST_POSITION_TAG = "PLAYLIST_POSITION";

    private MediaPlayer mp = new MediaPlayer();
    private ArrayList<Track> mTracks;
    private int mTrackIndex;

    TextView mArtistNameTextView;
    TextView mAlbumNameTextView;
    ImageView mAlbumArtImageView;
    TextView mTrackNameTextView;
    SeekBar mTrackSeekBar;
    ImageButton mPlayButton;
    ImageButton mPreviousButton;
    ImageButton mNextButton;

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

        if (intent.hasExtra(PLAYLIST_TRACKS_TAG)) {
            mTracks = intent.getParcelableArrayListExtra(PLAYLIST_TRACKS_TAG);
            mTrackIndex = intent.getIntExtra(PLAYLIST_POSITION_TAG, 0);
        } else {
            mTracks = args.getParcelableArrayList(PLAYLIST_TRACKS_TAG);
            mTrackIndex = args.getInt(PLAYLIST_POSITION_TAG, 0);
        }

        if (mTracks != null) {
            Track track = mTracks.get(mTrackIndex);

            // Load view with artist, album, and track information
            mArtistNameTextView = (TextView) rootView.findViewById(R.id.player_artist_name_textview);
            mAlbumNameTextView = (TextView) rootView.findViewById(R.id.player_album_name_textview);
            mAlbumArtImageView = (ImageView) rootView.findViewById(R.id.player_album_art_imageview);
            mTrackNameTextView = (TextView) rootView.findViewById(R.id.player_track_name_textview);
            mTrackSeekBar = (SeekBar) rootView.findViewById(R.id.player_seek_bar);
            mPlayButton = (ImageButton) rootView.findViewById(R.id.player_play_button);
            mPreviousButton = (ImageButton) rootView.findViewById(R.id.player_previous_button);
            mNextButton = (ImageButton) rootView.findViewById(R.id.player_next_button);

            loadView(track);

            // Autoplay track when view is loaded
            String audioUrl = track.previewUrl;
            playTrack(audioUrl);

            // Event listeners for audio controls
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        String uri = "android:drawable/ic_media_play";
                        int image = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        mPlayButton.setImageResource(image);

                        mp.pause();
                    } else {
                        String uri = "android:drawable/ic_media_pause";
                        int image = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        mPlayButton.setImageResource(image);

                        mp.start();
                    }
                }
            });

            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPreviousTrack();
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playNextTrack();
                }
            });

            mTrackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int totalDuration = mp.getDuration();
                    double currentPositionRatio = (double) seekBar.getProgress() / seekBar.getMax();
                    int newPosition = (int) ((double) currentPositionRatio * totalDuration);

                    mp.seekTo(newPosition);
                }
            });

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mTrackSeekBar.setMax(mp.getDuration());
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playNextTrack();
                }
            });

        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlayback();
    }

    private void stopPlayback() {
        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
    }

    private void playTrack(String audioUrl) {
        stopPlayback();
        try {
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(audioUrl);
            mp.prepare();
            if (!mp.isPlaying()) {
                mp.start();
            }
        } catch (IOException | IllegalArgumentException e) {
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Error streaming audio, please try later";
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();

            Log.e(LOG_TAG, "Error streaming audio");
            e.printStackTrace();
        }
    }

    private void playNextTrack() {
        if (mTrackIndex == mTracks.size() - 1) {
            mTrackIndex = 0;
            Track track = mTracks.get(mTrackIndex);
            loadView(track);
            playTrack(track.previewUrl);
        } else {
            mTrackIndex += 1;
            Track track = mTracks.get(mTrackIndex);
            loadView(track);
            playTrack(track.previewUrl);
        }
    }

    private void playPreviousTrack() {
        if (mTrackIndex == 0) {
            mTrackIndex = mTracks.size() - 1;
            Track track = mTracks.get(mTrackIndex);
            loadView(track);
            playTrack(track.previewUrl);
        } else {
            mTrackIndex -= 1;
            Track track = mTracks.get(mTrackIndex);
            loadView(track);
            playTrack(track.previewUrl);
        }
    }

    private void loadView(Track track) {
        mArtistNameTextView.setText(track.artistName);

        mAlbumNameTextView.setText(track.albumName);

        Picasso.with(getActivity())
                .load(track.albumImageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_help_black_24dp)
                .error(R.drawable.ic_help_black_24dp)
                .into(mAlbumArtImageView);

        mTrackNameTextView.setText(track.trackName);
    }

}
