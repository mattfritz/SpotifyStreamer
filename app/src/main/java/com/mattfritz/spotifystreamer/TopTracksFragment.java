package com.mattfritz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TopTracksFragment extends Fragment {

    private static final String LOG_TAG = TopTracksFragment.class.getSimpleName();
    private static final String QUERY_CACHE = "top_tracks";
    private final String ARTIST_ID_TAG = "ARTIST_ID";
    private final String ARTIST_NAME_TAG = "ARTIST_NAME";
    private final String PLAYER_FRAGMENT_TAG = "PLAYER_FRAGMENT_TAG";
    private final String SHOW_DIALOG_TAG = "SHOW_DIALOG";
    private final String PLAYLIST_TRACKS_TAG = "PLAYLIST_TRACKS";
    private final String PLAYLIST_POSITION_TAG = "PLAYLIST_POSITION";

    private SpotifyApi spotifyApi = new SpotifyApi();
    private TrackAdapter mTrackAdapter;
    private ArrayList<Track> mTracks;
    private ListView mListView;

    private Utility mUtility;

    public TopTracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mUtility = new Utility(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        Intent intent = getActivity().getIntent();
        Bundle arguments = getArguments();
        mTrackAdapter = new TrackAdapter(
                getActivity(),
                new ArrayList<Track>());

        mListView = (ListView) rootView.findViewById(R.id.listview_tracks);
        mListView.setAdapter(mTrackAdapter);

        String artistId = null;
        String artistName = null;

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
            artistName = intent.getStringExtra(Intent.EXTRA_TITLE);
        } else if (arguments != null) {
            artistId = arguments.getString(ARTIST_ID_TAG);
            artistName = arguments.getString(ARTIST_NAME_TAG);
        }

        if (artistId != null && artistName != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle("Top 10 Tracks");
            actionBar.setSubtitle(artistName);

            if (mUtility.isNetworkAvailable()) {
                FetchTopTracksTask task = new FetchTopTracksTask();
                task.execute(artistId);
            } else {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "No internet connection. Please connect and try again";
                int duration = Toast.LENGTH_SHORT;

                Toast.makeText(context, text, duration).show();
            }
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Refactor this two pane state into something more managable
                if (getActivity().findViewById(R.id.artist_fragment) != null) {
                    TrackPlayerFragment fragment = new TrackPlayerFragment();

                    // Only show dialog box if in two pane mode
                    Bundle args = new Bundle();
                    args.putBoolean(SHOW_DIALOG_TAG, true);
                    args.putInt(PLAYLIST_POSITION_TAG, position);
                    args.putParcelableArrayList(PLAYLIST_TRACKS_TAG, mTracks);
                    fragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .add(fragment, PLAYER_FRAGMENT_TAG)
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity(), TrackPlayerActivity.class);
                    intent.putExtra(PLAYLIST_POSITION_TAG, position);
                    intent.putExtra(PLAYLIST_TRACKS_TAG, mTracks);
                    startActivity(intent);
                }
            }
        });

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
