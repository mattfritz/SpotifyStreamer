package com.mattfritz.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;

public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String QUERY_CACHE = "term";

    private SpotifyService mService = new SpotifyApi().getService();
    private com.mattfritz.spotifystreamer.SpotifyApi api = new com.mattfritz.spotifystreamer.SpotifyApi();
    private ArrayList<com.mattfritz.spotifystreamer.Artist> mArtists;
    private ArtistAdapter mArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mArtistAdapter = new ArtistAdapter(
                getActivity(),
                new ArrayList<com.mattfritz.spotifystreamer.Artist>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.mattfritz.spotifystreamer.Artist artist = (com.mattfritz.spotifystreamer.Artist) parent.getItemAtPosition(position);
                Intent detailIntent = new Intent(getActivity(), TopTracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.id);
                startActivity(detailIntent);
            }
        });

        final SearchView searchView = (SearchView) rootView.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FetchArtistsTask task = new FetchArtistsTask();
                task.execute(query);
//                mService.searchArtists(query, new Callback<ArtistsPager>() {
//                    @Override
//                    public void success(ArtistsPager artistsPager, Response response) {
//                         mArtists = artistsPager.artists.items;
//                        if (mArtists != null) {
//                            mArtistAdapter.clear();
//                            mArtistAdapter.addAll(mArtists);
//
//                            if (mArtists.isEmpty()) {
//                                Context context = getActivity().getApplicationContext();
//                                CharSequence text = "No artists found, please refine your search";
//                                int duration = Toast.LENGTH_SHORT;
//
//                                Toast.makeText(context, text, duration).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Context context = getActivity().getApplicationContext();
//                        CharSequence text = "Could not retrieve artists";
//                        int duration = Toast.LENGTH_SHORT;
//
//                        Toast.makeText(context, text, duration).show();
//
//                        if (error.getResponse() != null) {
//                            Log.e(LOG_TAG, error.getMessage());
//                        }
//                    }
//                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Since Artist is not serializable, requery for results
        if (savedInstanceState != null) {
            mArtists = savedInstanceState.getParcelableArrayList(QUERY_CACHE);
            mArtistAdapter.clear();
            mArtistAdapter.addAll(mArtists);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(QUERY_CACHE, mArtists);
        super.onSaveInstanceState(outState);
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, ArrayList<com.mattfritz.spotifystreamer.Artist>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected ArrayList<com.mattfritz.spotifystreamer.Artist> doInBackground(String... params) {
            try {
                mArtists = api.searchArtists(params[0]);
                return mArtists;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<com.mattfritz.spotifystreamer.Artist> artists) {
            if (artists != null) {
                mArtistAdapter.clear();
                mArtistAdapter.addAll(artists);
            }
        }
    }

}
