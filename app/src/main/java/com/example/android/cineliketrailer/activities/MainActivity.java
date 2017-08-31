package com.example.android.cineliketrailer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cineliketrailer.model.MovieDetails;
import com.example.android.cineliketrailer.R;
import com.example.android.cineliketrailer.SettingsActivity;
import com.example.android.cineliketrailer.adapter.CustomMovieAdapter;
import com.example.android.cineliketrailer.async.AsyncTaskDelegator;
import com.example.android.cineliketrailer.async.FetchMovieTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskDelegator, SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mEmptyStateTextView;
    private View loadingIndicator;
    //private PreferenceManager preferenceScreen;

    CustomMovieAdapter customMovieAdapter;
    ArrayList<MovieDetails> movieDetalsArrayList = new ArrayList<>();

    final int UP_CODE = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar o menu; Isso adiciona itens à barra de ação se ela estiver presente.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // O item da barra de ação identificável clica aqui. A barra de ação
        // lida automaticamente com cliques no botão Home/Up, por tanto tempo
        // como você especifica uma atividade pai no AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, SettingsActivity.class), UP_CODE);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            startActivityForResult(new Intent(this, FavoriteActivity.class), UP_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String sort = sharedPreferences.getString(getString(R.string.pref_moviekey),
                getString(R.string.pref_popular));
        updateMovies(sort);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sort = prefs.getString(getString(R.string.pref_moviekey),
                getString(R.string.pref_popular));
        updateMovies(sort);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    private void updateMovies(String order) {
        if (checkConnection(this)) {
            FetchMovieTask getTask = new FetchMovieTask(this);
            getTask.execute(order);
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9E9E9E")));


        GridView gridView = (GridView) findViewById(R.id.gridview_movie);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);  //ID Check Connection
        gridView.setEmptyView(mEmptyStateTextView); //ID Check Connection

        loadingIndicator = findViewById(R.id.loading_indicator); //ID Check Loading
        gridView.setEmptyView(loadingIndicator); //ID Check Loading

        customMovieAdapter = new CustomMovieAdapter(this, movieDetalsArrayList);
        gridView.setAdapter(customMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MovieDetails movieDetails = customMovieAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                MovieDetails movie = new MovieDetails(movieDetails.getTitle(), movieDetails.getVote(),
                        movieDetails.getPosterUrl(), movieDetails.getOverView(), movieDetails.getRelease_date(),
                        movieDetails.getBackdrop_path(), movieDetails.getId(), movieDetails.getLanguage());
                intent.putExtra(MovieDetails.EXTRA_MOVIE_DETAILS, movie);
                startActivity(intent);
            }
        });
    }

    public boolean checkConnection(Context context) {  // Check Connection
        ConnectivityManager conMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isConnected());
    }

    @Override
    public void processFinish(Object output) {
        this.movieDetalsArrayList = (ArrayList<MovieDetails>) output;
        customMovieAdapter.updateData(this.movieDetalsArrayList);
    }

}
