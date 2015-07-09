package com.mattfritz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksActivityFragment extends Fragment {

    private static final String LOG_TAG = TopTracksActivityFragment.class.getSimpleName();

    private SpotifyService mService = new SpotifyApi().getService();


    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        Intent intent = getActivity().getIntent();
        final TrackAdapter trackAdapter = new TrackAdapter(
                getActivity(),
                new ArrayList<Track>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_tracks);
        listView.setAdapter(trackAdapter);

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);

            // Add country code until Spotify wrapper is patched
            // https://github.com/kaaes/spotify-web-api-android/pull/83
            Map<String, Object> options = new Hashtable<String, Object>();
            options.put("country", "US");

            mService.getArtistTopTrack(artistId, options, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    List<Track> tracksList = tracks.tracks;
                    if (tracksList != null) {
                        trackAdapter.clear();
                        trackAdapter.addAll(tracksList);

                        if (tracksList.isEmpty()) {
                            Context context = getActivity().getApplicationContext();
                            CharSequence text = "No top tracks found for this artist";
                            int duration = Toast.LENGTH_SHORT;

                            Toast.makeText(context, text, duration).show();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = "Could not retrieve top tracks";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(context, text, duration).show();

                    if (error.getResponse() != null) {
                        Log.e(LOG_TAG, error.getMessage());
                    }
                }
            });
        }
        return rootView;
    }
}
