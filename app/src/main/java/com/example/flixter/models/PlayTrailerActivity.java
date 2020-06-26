package com.example.flixter.models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class PlayTrailerActivity extends AppCompatActivity {
    public static final String URL_PREF = "https://api.themoviedb.org/3/movie/";
    public static final String URL_MID = "/videos?api_key=";
    public static final String URL_SUF = "&language=en-US";
    public static String vidKey;
    public static String TAG = "PlayTrailerActivity";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_trailer);

        Log.d("PlayTrailerActivity", "Activity started");
        // Unwrap movie passed by intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        playVideo(movie);
    }

    public void playVideo(Movie movie) {
        // TODO make void?
        // Create MdbUrl for request
        String mdbUrl = getMdbUrl(movie);
        Log.d("MDB URL", mdbUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        //Request the data from database;
        client.get(mdbUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Get the results JSON array from MDB API
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject vidObj = (JSONObject) results.get(0);
                    Log.d("Results 0 ", vidObj.toString());

                    // Get the key to make the youtube URL
                    vidKey = vidObj.getString("key");
                    Log.d("Video key", vidKey);
                    String youtubeURL =  makeYoutubeURL(vidKey);
                    Log.d("Youtube URL", youtubeURL);





                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception (video)", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
        return;
    }

    public String getMdbUrl(Movie movie) {
        String apiKey = getString(R.string.mdbKey);
        Log.d("MDB Key", apiKey);
        return URL_PREF + movie.getId() + URL_MID + apiKey + URL_SUF;
    }

    public static String makeYoutubeURL(String key) {
        return "https://www.youtube.com/watch?v=" + key;
    }
}