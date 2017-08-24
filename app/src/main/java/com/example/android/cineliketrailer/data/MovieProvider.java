package com.example.android.cineliketrailer.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.util.StringBuilderPrinter;

/**
 * Created by alexbitencourt on 19/06/17.
 */
public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIE_SINGLE = 101;
    static final int FAVORITE = 102;
    static final int FAVORITE_SINGLE = 103;

    private static final SQLiteQueryBuilder sMovieByFavoriteQueryBuilder;

    static{
        sMovieByFavoriteQueryBuilder = new SQLiteQueryBuilder();

        sMovieByFavoriteQueryBuilder.setTables(
                MovieContract.MoviesEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.FavoriteEntry.TABLE_NAME +
                        " ON " + MovieContract.MoviesEntry.TABLE_NAME +
                        " = " + MovieContract.FavoriteEntry.TABLE_NAME +
                        "." + MovieContract.FavoriteEntry._ID);
    }


        private static final String sMovieSetting =
                MovieContract.MoviesEntry.TABLE_NAME +
                        "." + MovieContract.MoviesEntry.COLUMN_MOVIE_ID;

        private static final String sMovieFavoriteSetting =
                MovieContract.FavoriteEntry.TABLE_NAME +
                        "." + MovieContract.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID;


    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {

        String movieSetting = MovieContract.MoviesEntry.getDbIdFrmUri(uri);

        return mOpenHelper.getWritableDatabase().query(MovieContract.MoviesEntry.TABLE_NAME,
                projection,
                sMovieSetting,
                new String[]{movieSetting},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieByFavorites(String[] projection, String sortOrder) {

        return mOpenHelper.getReadableDatabase().query(MovieContract.FavoriteEntry.TABLE_NAME,
                projection,
                sMovieFavoriteSetting,
                null,
                null,
                null,
                sortOrder
        );
    }


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*", MOVIE_SINGLE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE + "/*", FAVORITE_SINGLE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MovieContract.MoviesEntry.CONTENT_TYPE;
            case MOVIE_SINGLE:
                return MovieContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case FAVORITE:
                return MovieContract.FavoriteEntry.CONTENT_TYPE;
            case FAVORITE_SINGLE:
                return MovieContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                retCursor = getMovieById(uri, projection, sortOrder);
                break;
            }
            case MOVIE_SINGLE: {
                retCursor = getMovieById(uri, projection, sortOrder);
                break;
            }
            case FAVORITE: {
                retCursor = getMovieByFavorites(projection, sortOrder);
                break;
            }
            case FAVORITE_SINGLE: {
                retCursor = getMovieByFavorites(projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = MovieContract.MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_SINGLE: {
                long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = MovieContract.MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE: {
                long _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = MovieContract.FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
                case FAVORITE_SINGLE: {
                    long _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, contentValues);
                    if (_id > 0)
                        returnUri = MovieContract.FavoriteEntry.buildFavoriteUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case FAVORITE:
                rowsDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_SINGLE:
                selection = MovieContract.FavoriteEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Execution error in delete method.");
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    /*
    @Override
    public int deleteFavorite(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == selection) selection = "1";
        rowsDeleted = db.delete(
                MovieContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    } */


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_SINGLE:
                rowsUpdated = db.update(MovieContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITE:
                rowsUpdated = db.update(MovieContract.FavoriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITE_SINGLE:
                rowsUpdated = db.update(MovieContract.FavoriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }


    // Método opcional
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

}
