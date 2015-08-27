package com.mattfritz.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ArtistsActivity extends AppCompatActivity {

    private final String LOG_TAG = ArtistsActivity.class.getSimpleName();
    private final String TRACKSFRAGMENT_TAG = "TFTAG";
    private boolean mTwoPane;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.top_tracks_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.top_tracks_container,
                                new TopTracksFragment(),
                                TRACKSFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        ArtistsFragment mf = ((ArtistsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.artist_fragment));

        mf.setTwoPane(mTwoPane);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
