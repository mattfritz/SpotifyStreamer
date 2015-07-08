package com.mattfritz.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    ArtistAdapter artistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        SearchSpotifyTask task = new SearchSpotifyTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        artistAdapter = new ArtistAdapter(
                getActivity(),
                new ArrayList<Artist>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
        listView.setAdapter(artistAdapter);

        return rootView;
    }

    public class SearchSpotifyTask extends AsyncTask<Void, Void, List<Artist>>
    {
        @Override
        protected List<Artist> doInBackground(Void... strings) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            ArtistsPager results = service.searchArtists("Paul");
            return results.artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (artists != null) {
                artistAdapter.clear();
                artistAdapter.addAll(artists);
            }
        }
    }
}
