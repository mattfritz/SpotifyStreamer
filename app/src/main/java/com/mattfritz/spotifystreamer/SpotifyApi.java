package com.mattfritz.spotifystreamer;

import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SpotifyApi {
    private final OkHttpClient client = new OkHttpClient();

    public ArrayList<Artist> searchArtists(String query) throws Exception {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.spotify.com")
                .appendPath("v1")
                .appendPath("search")
                .appendQueryParameter("type", "artist")
                .appendQueryParameter("q", query);

        String url = builder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String result = response.body().string();
        ArrayList<Artist> artists = new ArrayList<>();

        JSONObject jsonBody = new JSONObject(result).getJSONObject("artists");
        JSONArray artistsJson = jsonBody.getJSONArray("items");

        for (int i = 0; i < artistsJson.length(); i++) {
            String imageUrl;
            JSONObject artist = artistsJson.getJSONObject(i);
            String artistName = artist.getString("name");
            JSONArray images = artist.getJSONArray("images");
            try {
                imageUrl = images.getJSONObject(0).getString("url");
            } catch (Exception e) {
                imageUrl = null;
            }
            String id = artist.getString("id");
            artists.add(new Artist(artistName, imageUrl, id));
        }

        return artists;
    }
}
