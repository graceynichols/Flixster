package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.Target;
import com.example.flixter.MovieDetailsActivity;
import com.example.flixter.R;
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
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
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
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#c7ebe8"));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get the elements of your layout
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
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
            int radius;
            int margin;
            // Change image depending on phone orientation
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Phone is in landscape
                radius = 60;
                margin = 0;
                imageUrl = movie.getBackdropPath();
                Glide.with(context).load(imageUrl)
                        .placeholder(R.drawable.flicks_backdrop_placeholder)
                        .error(R.drawable.flicks_backdrop_placeholder)
                        .transform(new RoundedCornersTransformation(radius,
                        margin)).fitCenter().into(ivPoster);
            } else {
                radius = 20;
                margin = 0;
                imageUrl = movie.getPosterPath();
                Glide.with(context).load(imageUrl).placeholder(R.drawable.flicks_movie_placeholder)
                        .error(R.drawable.flicks_movie_placeholder).transform(new RoundedCornersTransformation(radius,
                        margin)).override(Target.SIZE_ORIGINAL).into(ivPoster);
            }

            //


            //Glide.with(context).load(imageUrl).into(ivPoster);

        }


    }
}
