package com.mattfritz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TopTracksActivityFragment extends Fragment {

    private static final String LOG_TAG = TopTracksActivityFragment.class.getSimpleName();
    private static final String QUERY_CACHE = "top_tracks";
    private SpotifyApi spotifyApi = new SpotifyApi();
    private TrackAdapter mTrackAdapter;
    private ArrayList<Track> mTracks;
    private ListView mListView;


    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        Intent intent = getActivity().getIntent();
        mTrackAdapter = new TrackAdapter(
                getActivity(),
                new ArrayList<Track>());

        mListView = (ListView) rootView.findViewById(R.id.listview_tracks);
        mListView.setAdapter(mTrackAdapter);

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
            String artistName = intent.getStringExtra(Intent.EXTRA_TITLE);

            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionBar.setSubtitle(artistName);

            FetchTopTracksTask task = new FetchTopTracksTask();
            task.execute(artistId);
        }

        if (savedInstanceState != null) {
            mTracks = savedInstanceState.getParcelableArrayList(QUERY_CACHE);
            int top = savedInstanceState.getInt("list_position_top");
            int index = savedInstanceState.getInt("list_position_index");
            mTrackAdapter.clear();
            // TODO: Make sure mTracks always has a value
            if (mTracks != null) {
                mTrackAdapter.addAll(mTracks);
                mListView.setSelectionFromTop(index, top);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int top;
        int index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(index);
        if (v == null) {
            top = 0;
        } else {
            top = (v.getTop() - mListView.getPaddingTop());
        }

        outState.putInt("list_position_top", top);
        outState.putInt("list_position_index", index);
        outState.putParcelableArrayList(QUERY_CACHE, mTracks);
        super.onSaveInstanceState(outState);
    }

    public class FetchTopTracksTask extends AsyncTask<String, Void, ArrayList<Track>> {

        private final String LOG_TAG = FetchTopTracksTask.class.getSimpleName();

        @Override
        protected ArrayList<Track> doInBackground(String... params) {
            try {
                mTracks = spotifyApi.getArtistTopTracks(params[0]);
                return mTracks;
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Track> tracks) {
            if (tracks != null) {
                mTrackAdapter.clear();
                mTrackAdapter.addAll(tracks);

                if (tracks.isEmpty()) {
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = "No top tracks found for this artists";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(context, text, duration).show();
                }
            } else {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Could not retrieve top tracks";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(context, text, duration).show();
            }
        }
    }
}
