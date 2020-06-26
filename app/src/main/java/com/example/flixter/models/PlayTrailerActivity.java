package com.example.flixter.models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class PlayTrailerActivity extends YouTubeBaseActivity {
    public static final String URL_PREF = "https://api.themoviedb.org/3/movie/";
    public static final String URL_MID = "/videos?api_key=";
    public static final String URL_SUF = "&language=en-US";
    public static String vidKey;
    public static String TAG = "PlayTrailerActivity";
    String youtubeKey;
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
                    youtubeKey = getString(R.string.youtubeKey);
                    vidKey = vidObj.getString("key");
                    Log.d("Video key", vidKey);
                    Log.d("Youtube key", youtubeKey);

                    YouTubePlayerView playerView = findViewById(R.id.player);

                    // initialize with API key stored in secrets.xml
                    playerView.initialize(getString(R.string.youtubeKey), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.loadVideo(vidKey);
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                        }
                    });
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
}