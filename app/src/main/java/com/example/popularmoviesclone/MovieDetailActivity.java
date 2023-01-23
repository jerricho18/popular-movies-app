package com.example.popularmoviesclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmoviesclone.adapters.MovieListAdapter;
import com.example.popularmoviesclone.adapters.ReviewListAdapter;
import com.example.popularmoviesclone.adapters.TrailerListAdapter;
import com.example.popularmoviesclone.database.MovieDatabase;
import com.example.popularmoviesclone.databinding.MovieDetailLayoutBinding;
import com.example.popularmoviesclone.models.Movie;
import com.example.popularmoviesclone.models.Review;
import com.example.popularmoviesclone.models.Trailer;
import com.example.popularmoviesclone.utils.Constants;
import com.example.popularmoviesclone.utils.Utils;
import com.example.popularmoviesclone.viewmodels.FavoriteMovieViewModel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements TrailerListAdapter.TrailerItemClickListener {

    RequestQueue queue;
    Gson gson;

    private MovieDatabase movieDatabase;

    private MovieDetailLayoutBinding binding;
    private Movie movie;
    private RecyclerView reviewRecyclerView, trailerRecyclerView;
    private ReviewListAdapter reviewListAdapter;
    private TrailerListAdapter trailerListAdapter;

    private ImageView favoriteButton;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.movie_detail_layout);
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        movie = getIntent().getParcelableExtra("movie");
        initUI();
        initHttpRequestComponents();
        getReviewData();
        getTrailerData();

        checkIfMovieIsInFavoriteList();
    }

    private void checkIfMovieIsInFavoriteList() {
        Movie m = movieDatabase.movieDao().getMovieById(this.movie.getId());
        if (m != null) {
            isFavorite = true;
            favoriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    private void initHttpRequestComponents() {
        queue = Volley.newRequestQueue(this);
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private void getReviewData() {
        String url = Constants.BASE_URL + "/" + movie.getId() + "/reviews?api_key=" + Constants.API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Log.d("JER", String.valueOf(results));
                        List<Review> reviewList = new ArrayList<>();
                        for (int i=0; i<results.length(); i++) {
                            Log.d("JER", results.getString(i));
                            Review review = gson.fromJson(results.getString(i), Review.class);
                            reviewList.add(review);
                        }
                        reviewListAdapter.setReviewList(reviewList);
                    } catch (JSONException e) {
                        Log.d("JER", "ERROR");
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("JER", "ERROR");
                }
        );
        queue.add(request);
    }

    private void getTrailerData() {
        String url = Constants.BASE_URL + "/" + movie.getId() + "/videos?api_key=" + Constants.API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Log.d("JER", String.valueOf(results));
                        List<Trailer> trailerList = new ArrayList<>();
                        for (int i=0; i<results.length(); i++) {
                            Log.d("JER", results.getString(i));
                            Trailer trailer = gson.fromJson(results.getString(i), Trailer.class);
                            trailerList.add(trailer);
                        }
                        trailerListAdapter.setTrailerList(trailerList);
                    } catch (JSONException e) {
                        Log.d("JER", "ERROR");
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("JER", "ERROR");
                }
        );
        queue.add(request);
    }


    public void initUI() {
        String imageURL = Constants.BASE_IMAGE_URL + movie.getPosterPath();
        String rating = movie.getVoteAverage() + " / 10";

        Picasso.get().load(imageURL).into(binding.ivMovieDetailPoster);

        binding.tvMovieDetailTitle.setText(movie.getTitle());
        binding.tvMovieDetailOverview.setText(movie.getOverview());
        binding.tvMovieDetailReleaseDate.setText(movie.getReleaseDate());
        binding.tvMovieDetailRating.setText(rating);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewListAdapter = new ReviewListAdapter();
        reviewRecyclerView = findViewById(R.id.rv_reviews);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setAdapter(reviewListAdapter);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerListAdapter = new TrailerListAdapter(this);
        trailerRecyclerView = findViewById(R.id.rv_trailers);
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerRecyclerView.setAdapter(trailerListAdapter);
        trailerRecyclerView.setNestedScrollingEnabled(false);

        favoriteButton = findViewById(R.id.iv_favorite_button);
        favoriteButton.setOnClickListener(view -> {
            if (isFavorite) {
                isFavorite = false;
                favoriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                movieDatabase.movieDao().delete(movie);
            } else {
                isFavorite = true;
                favoriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                movieDatabase.movieDao().insert(movie);
            }
        });
    }

    @Override
    public void onTrailerItemClick(int clickedItemIndex) {
        String key = trailerListAdapter.getTrailerByIndex(clickedItemIndex).getKey();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}