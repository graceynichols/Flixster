package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String URL_PREF = "https://api.themoviedb.org/3/movie/";
    public static final String URL_MID = "/videos?api_key=";
    public static final String URL_SUF = "&language=en-US";
    public static final String TAG = "Movies";
    public static String vidKey;
    Movie movie;

    // View objects
    TextView tvTitle;
    TextView tvOverview;
    TextView popularity;
    ImageView thumb;
    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resolve the view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        popularity = findViewById(R.id.popularity);
        thumb = findViewById(R.id.thumb);

        // Unwrap the movie passed by intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // Set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        popularity.setText("" + movie.getPop());

        // Set thumbnail for video
        String imageUrl = movie.getBackdropPath();
        Glide.with(this).load(imageUrl)
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder).fitCenter().into(thumb);
        // Display vote average (divided by 2 since it's out of 10)
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        // Attach on click listener to thumbnail
        findViewById(R.id.thumb).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launch PlayTrailerActivity
                playVideo(movie);
            }
        });
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

    public static String makeYoutubeURL(String key) {
        return "https://www.youtube.com/watch?v=" + key;
    }

    public String getMdbUrl(Movie movie) {
        String apiKey = getString(R.string.mdbKey);
        Log.d("MDB Key", apiKey);
        return URL_PREF + movie.getId() + URL_MID + apiKey + URL_SUF;
    }
}