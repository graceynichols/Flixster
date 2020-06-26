package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.Target;
import com.example.flixter.MovieDetailsActivity;
import com.example.flixter.R;
import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.databinding.ItemMovieBinding;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflate a layout and return it inside a viewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemMovieBinding binding = ItemMovieBinding.inflate(inflater);
        Log.d("MovieAdapter", "onCreateViewHolder");
        return new ViewHolder(binding);
    }

    // Populate data into the view through the viewholder (take data at that position and put it
    // In the view contained by the viewholder)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the position
        Movie movie = movies.get(position);
        // Bind the movie data into the view holder
        holder.bind(movie);
        // Alternate colors of recyclerview rows
        if(position %2 == 1)
        {
            // White
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else
        {
            // Light blue
            holder.itemView.setBackgroundColor(Color.parseColor("#f0fcfa"));
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        // Binding class for a row in recycler view
        ItemMovieBinding binding;

        public ViewHolder(ItemMovieBinding b) {
            super(b.getRoot());
            // Get the elements of your layout
            tvTitle = b.tvTitle;
            tvOverview = b.tvOverview;
            ivPoster = b.ivPoster;
            binding = b;

            // add this as the itemView's OnClickListener
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get position
            int position = getAdapterPosition();
            // Check if position is valid
            if (position != RecyclerView.NO_POSITION) {
                // Get the movie at the position
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // Serialize the movie using parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            // Use glide to render images
            String imageUrl;
            int radius = 35;
            int margin = 0;
            RequestBuilder <Drawable> image;
            // Change image depending on phone orientation
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Phone is in landscape
                imageUrl = movie.getBackdropPath();
                image = Glide.with(context).load(imageUrl)
                        .placeholder(R.drawable.flicks_backdrop_placeholder)
                        .error(R.drawable.flicks_backdrop_placeholder);

            } else {
                imageUrl = movie.getPosterPath();
                image = Glide.with(context).load(imageUrl).placeholder(R.drawable.flicks_movie_placeholder)
                        .error(R.drawable.flicks_movie_placeholder);
            }
            // Round corners
            image.fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster);
        }
    }
}
