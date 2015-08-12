package com.mattfritz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String QUERY_CACHE = "term";

    private SpotifyApi spotifyApi = new SpotifyApi();
    private ArrayList<Artist> mArtists;
    private ArtistAdapter mArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mArtistAdapter = new ArtistAdapter(
                getActivity(),
                new ArrayList<Artist>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) parent.getItemAtPosition(position);
                Intent detailIntent = new Intent(getActivity(), TopTracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.id)
                        .putExtra(Intent.EXTRA_TITLE, artist.name);
                startActivity(detailIntent);
            }
        });

        final SearchView searchView = (SearchView) rootView.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FetchArtistsTask task = new FetchArtistsTask();
                task.execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (savedInstanceState != null) {
            mArtists = savedInstanceState.getParcelableArrayList(QUERY_CACHE);

            // TODO: Make sure mArtists is never null
            if (mArtists != null) {
                mArtistAdapter.clear();
                mArtistAdapter.addAll(mArtists);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(QUERY_CACHE, mArtists);
        super.onSaveInstanceState(outState);
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, ArrayList<Artist>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected ArrayList<Artist> doInBackground(String... params) {
            try {
                mArtists = spotifyApi.searchArtists(params[0]);
                return mArtists;
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Artist> artists) {
            if (artists != null) {
                mArtistAdapter.clear();
                mArtistAdapter.addAll(artists);

                if (artists.isEmpty()) {
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = "No artists found, please refine your search";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(context, text, duration).show();
                }
            } else {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Could not retrieve artists";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(context, text, duration).show();
            }
        }
    }

}
