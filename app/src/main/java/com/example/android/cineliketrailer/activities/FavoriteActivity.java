package com.example.android.cineliketrailer.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cineliketrailer.R;
import com.example.android.cineliketrailer.adapter.FavoriteAdapter;
import com.example.android.cineliketrailer.async.AsyncTaskDelegator;
import com.example.android.cineliketrailer.async.FetchMovieTask;
import com.example.android.cineliketrailer.data.MovieContract;
import com.example.android.cineliketrailer.model.MovieDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;

/**
 * Created by alexbitencourt on 09/08/17.
 */

public class FavoriteActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = FavoriteActivity.class.getName();

    private FavoriteAdapter favoriteAdapter;
    private List<MovieDetails> movieDetails = new ArrayList<>();
    private static final int FAVORITE_LOADER = 2;
    private TextView mEmptyStateTextView;


    private static final String[] DETAIL_COLUMNS = {
            MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry._ID,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_TITLE,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_RELEASE_DATE,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_BACKDROP_PATH,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_VOTE_AVERAGE,
            MovieContract.FavoriteEntry.COLUMN_FAVORITE_LANGUAGE,

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
<<<<<<< HEAD
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
=======

        /*
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9E9E9E")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); */
>>>>>>> github/master

        GridView gridView = (GridView) findViewById(R.id.gridview_movie);

        favoriteAdapter = new FavoriteAdapter(this, null);
        gridView.setAdapter(favoriteAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);  //ID Check Connection
        gridView.setEmptyView(mEmptyStateTextView); //ID Check Connection

        getSupportLoaderManager().initLoader(FAVORITE_LOADER, null, this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("favoritesList", (ArrayList<? extends Parcelable>) movieDetails);
        super.onSaveInstanceState(outState);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new android.support.v4.content.CursorLoader(this,
                MovieContract.FavoriteEntry.CONTENT_URI,
                DETAIL_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        favoriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        favoriteAdapter.swapCursor(null);
    }

}
