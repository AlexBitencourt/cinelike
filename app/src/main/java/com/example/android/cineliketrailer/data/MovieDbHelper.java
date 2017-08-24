package com.example.android.cineliketrailer.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.android.cineliketrailer.activities.FavoriteActivity;
import com.example.android.cineliketrailer.data.MovieContract.MoviesEntry;
import com.example.android.cineliketrailer.data.MovieContract.FavoriteEntry;
import com.example.android.cineliketrailer.model.MovieDetails;

/**
 * Created by alexbitencourt on 19/06/17.
 */
public class MovieDbHelper extends SQLiteOpenHelper {


    private static final String LOG_TAG = MovieDbHelper.class.getSimpleName();


    /*
     * Incrementamos a versão a cada alteração no esquema do DB.
     */
    private static final int DATABASE_VERSION = 3;

    /*
     * Nome da base do banco de dados.
     */
    static final String DATABASE_NAME = "movies.db";

    /*
     * Construtor da classe MovieDbHelper.
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTERGER PRIMARY KEY," +
                MoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_LANGUAGE + " TEXT NOT NULL " +
                //MoviesEntry.COLUMN_FAVORITE + " TEXT NOT NULL " +
        " );";


        final String SQL_CREATE_MOVIES_TABLE_FAVORITE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTERGER PRIMARY KEY," +
                FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_FAVORITE_LANGUAGE + " TEXT NOT NULL " +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    /*
     * Método delete filmes Favoritos
     */
    /*
    public void deleteFavorite() {
        long movieId = 0;
        String selection = FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(movieId)};
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
    } */
}
