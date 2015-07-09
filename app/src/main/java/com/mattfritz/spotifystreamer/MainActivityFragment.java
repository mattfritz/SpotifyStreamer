package com.mattfritz.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private SpotifyService mService = new SpotifyApi().getService();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ArtistAdapter artistAdapter = new ArtistAdapter(
                getActivity(),
                new ArrayList<Artist>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
        listView.setAdapter(artistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) parent.getItemAtPosition(position);
                Intent detailIntent = new Intent(getActivity(), TopTracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.id);
                startActivity(detailIntent);
            }
        });

        final SearchView searchView = (SearchView) rootView.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mService.searchArtists(query, new Callback<ArtistsPager>() {
                    @Override
                    public void success(ArtistsPager artistsPager, Response response) {
                        List<Artist> artists = artistsPager.artists.items;
                        if (artists != null) {
                            artistAdapter.clear();
                            artistAdapter.addAll(artists);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // TODO: show a toast or add a custom textview to indicate failure
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }
}
