package com.example.popularmoviesclone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.popularmoviesclone.models.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static MovieDatabase INSTANCE;
    private static String DATABASE_NAME = "favorite_movie_database";

    public static MovieDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MovieDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
