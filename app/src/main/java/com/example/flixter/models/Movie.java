package com.example.flixter.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;



@Parcel
public class Movie {
    /*
    public static final String CONFIGURATIONS_URL =
            "https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG_CONFIG = "Configuration";*/
    public static final String TAG = "Movies";
    Double voteAverage;
    String posterPath;
    String backdropPath;
    String title;
    String overview;
    String vidKey;
    Double pop;
    Integer id;

    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        // Extract movie's attributes from json object representing movie
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        pop = jsonObject.getDouble("popularity");
        id = jsonObject.getInt("id");
    }

    // Creates list of movie objects from json array
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getBackdropPath() {
        // TODO get prefix from api instead of hardcoding
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() {
        /*
        // Get configurations from movie database
        AsyncHttpClient client = new AsyncHttpClient();
        // Request the data from database
        client.get(CONFIGURATIONS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG_CONFIG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG_CONFIG, "Results: " + results.toString());
                    // Turn json array into list of movie objects
                    String base = jsonObject.ge
                } catch (JSONException e) {
                    Log.e(TAG_CONFIG, "Hit json exception", e);
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });*/
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Double getPop() { return pop; }

    public int getId() { return id; }
}
