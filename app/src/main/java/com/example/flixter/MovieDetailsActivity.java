package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.models.Movie;
import com.example.flixter.models.PlayTrailerActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailsActivity";
    String mdbUrl;
    Context context;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        context = this;

        // Unwrap the movie passed by intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // Set title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        binding.popularity.setText("" + movie.getPop());

        // Set thumbnail for video
        String imageUrl = movie.getBackdropPath();
        Glide.with(this).load(imageUrl)
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder).fitCenter().into(binding.thumb);
        // Display vote average (divided by 2 since it's out of 10)
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // Display cast
        mdbUrl = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/credits?api_key=" +
                getString(R.string.mdbKey);
        Log.d("MDB Cast URL", mdbUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        //Request the cast from database;
        client.get(mdbUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                TextView[] views = new TextView[]{binding.cast1, binding.cast2, binding.cast3};
                try {
                    // Get the results JSON array from MDB API
                    JSONArray results = jsonObject.getJSONArray("cast");
                    Log.d("Cast list", results.toString());
                    // Just in case cast size < 3
                    for (int i = 0; i < 3 && i < results.length(); i++) {
                        views[i].setText(((JSONObject) results.get(i)).getString("name"));
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception (cast)", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // Attach on click listener to thumbnail
        binding.thumb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launch Play Trailer activity
                Intent intent = new Intent(context, PlayTrailerActivity.class);
                // Serialize the movie using parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);

            }
        });
    }




}