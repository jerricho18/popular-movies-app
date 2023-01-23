package com.example.popularmoviesclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmoviesclone.adapters.MovieListAdapter;
import com.example.popularmoviesclone.models.Movie;
import com.example.popularmoviesclone.utils.Constants;
import com.example.popularmoviesclone.utils.Utils;
import com.example.popularmoviesclone.viewmodels.FavoriteMovieViewModel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener {

    private RequestQueue queue;
    private Gson gson;

    private RecyclerView recyclerView;
    private MovieListAdapter movieListAdapter;
    private boolean isInFavoriteMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHttpRequestComponents();

        if (Utils.isNetworkAvailable(this)) {
            inquiryMovieData("popular");
        } else {
            System.out.println("NO INTERNET");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieListAdapter = new MovieListAdapter(this);
        recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieListAdapter);

        setupFavoriteMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        isInFavoriteMenu = false;

        switch (item.getItemId()) {
            case R.id.popular_menu:
                inquiryMovieData("popular");
                return true;
            case R.id.top_rated_menu:
                inquiryMovieData("top_rated");
                return true;
            case R.id.favorite_menu:
                isInFavoriteMenu = true;
                setupFavoriteMovieData();
                return true;
            default:
                return true;
        }
    }

    private void initHttpRequestComponents() {
        queue = Volley.newRequestQueue(this);
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private void inquiryMovieData(String searchBy) {
        Log.d("JER", "MASUK INQUIRY");
        String url = Constants.BASE_URL + "/" + searchBy + "?api_key=" + Constants.API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Log.d("JER", "DAPET RESPONSE");
                        List<Movie> movieList = new ArrayList<>();
                        for (int i=0; i<results.length(); i++) {
                            Movie movie = gson.fromJson(results.getString(i), Movie.class);
                            movieList.add(movie);
                        }
                        movieListAdapter.setMovieList(movieList);
                    } catch (JSONException e) {
                        Log.d("JER", "ERROR");
                        e.printStackTrace();
                    }

                },
                error -> {
                    Log.d("JER", String.valueOf(error));
                }
        );
        queue.add(request);
    }

    private void setupFavoriteMovieData() {
        FavoriteMovieViewModel favoriteMovieViewModel = new ViewModelProvider(this).get(FavoriteMovieViewModel.class);
        favoriteMovieViewModel.getFavoriteMovieList().observe(this, movieList -> {
            if (isInFavoriteMenu) movieListAdapter.setMovieList(movieList);
        });
    }

    @Override
    public void onMovieItemClick(int clickedItemIndex) {
        Log.d("JER", "Clicked index: " + clickedItemIndex);
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie", movieListAdapter.getMovieByIndex(clickedItemIndex));
        startActivity(intent);
    }
}