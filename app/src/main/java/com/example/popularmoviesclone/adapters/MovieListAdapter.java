package com.example.popularmoviesclone.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesclone.R;
import com.example.popularmoviesclone.models.Movie;
import com.example.popularmoviesclone.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter {

    private List<Movie> movieList = new ArrayList<>();
    final private MovieItemClickListener onClickListener;

    public MovieListAdapter(MovieItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(int clickedItemIndex);
    }

    public void setMovieList(List<Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }

    public Movie getMovieByIndex(int index) {
        return movieList.get(index);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieItemViewHolder) holder).bindData(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView posterImageView;
        private TextView titleTextView;

        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);

            posterImageView = itemView.findViewById(R.id.iv_movie_poster);
            titleTextView = itemView.findViewById(R.id.tv_movie_title);
            posterImageView.setOnClickListener(this);
        }

        public void bindData(Movie movie) {
            String imageURL = Constants.BASE_IMAGE_URL + movie.getPosterPath();
            Log.d("JER", imageURL);
            titleTextView.setText(movie.getTitle());
            Picasso.get().load(imageURL).into(posterImageView);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onMovieItemClick(clickedPosition);
        }
    }
}
