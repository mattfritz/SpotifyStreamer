package com.mattfritz.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrackPlayerFragment extends DialogFragment {

    private final String SHOW_DIALOG_TAG = "SHOW_DIALOG";

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
        return inflater.inflate(R.layout.fragment_track_player, container, false);
    }
}
