package com.example.android.cineliketrailer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alexbitencourt on 19/06/17.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.cinelike";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITE = "favorites";


    /* Classe interna que define o conteúdo da tabela de filmes */
    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Table name
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_LANGUAGE = "language";

        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getDbIdFrmUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;


        // Table name
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_FAVORITE_MOVIE_ID = "movie_id";
        public static final String COLUMN_FAVORITE_TITLE = "title";
        public static final String COLUMN_FAVORITE_OVERVIEW = "overview";
        public static final String COLUMN_FAVORITE_RELEASE_DATE = "release_date";
        public static final String COLUMN_FAVORITE_POSTER_PATH = "poster_path";
        public static final String COLUMN_FAVORITE_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_FAVORITE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_FAVORITE_LANGUAGE = "language";


        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getDbIdFrmUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

}