package com.mattfritz.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArtistAdapter artistAdapter;

    Artist[] artists = {
            new Artist("Test Dude", "https://i.scdn.co/image/96e2e59a1bf0b90cce97035ca48ad017cb9937c9"),
            new Artist("Test Another", "https://i.scdn.co/image/96e2e59a1bf0b90cce97035ca48ad017cb9937c9"),
            new Artist("Test Bono", "https://i.scdn.co/image/96e2e59a1bf0b90cce97035ca48ad017cb9937c9"),
            new Artist("Test Wat", "https://i.scdn.co/image/96e2e59a1bf0b90cce97035ca48ad017cb9937c9")
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        artistAdapter = new ArtistAdapter(getActivity(), Arrays.asList(artists));

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
        listView.setAdapter(artistAdapter);

        return rootView;
    }
}
