package com.example.popularmoviesclone.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmoviesclone.database.MovieDatabase;
import com.example.popularmoviesclone.models.Movie;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favoriteMovieList;

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        favoriteMovieList = MovieDatabase.getInstance(application.getApplicationContext())
                .movieDao()
                .getAllMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovieList() { return  favoriteMovieList; }

}
