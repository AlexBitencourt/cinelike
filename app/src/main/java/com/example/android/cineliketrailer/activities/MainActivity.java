package com.example.android.cineliketrailer.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.example.android.cineliketrailer.adapter.CustomMovieAdapter;
import com.example.android.cineliketrailer.async.AsyncTaskDelegator;
import com.example.android.cineliketrailer.async.FetchMovieTask;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements AsyncTaskDelegator {

    private TextView mEmptyStateTextView;
    private View loadingIndicator;

    private CustomMovieAdapter customMovieAdapter;
    private ArrayList<MovieDetails> movieDetalsArrayList = new ArrayList<>();
    private String movies_order = "popular";

    private final int UP_CODE = 1;

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

        if (id == R.id.ordem_mais_popular) {
            movies_order = getString(R.string.order_option_popular);
            updateMovies(movies_order);
            item.setChecked(true);
            return true;
        }
        if (id == R.id.ordem_mais_votados) {
            movies_order = getString(R.string.order_option_toprated);
            updateMovies(movies_order);
            item.setChecked(true);
            return true;
        }
        if (id == R.id.ordem_favoritos) {
            startActivityForResult(new Intent(this, FavoriteActivity.class), UP_CODE);
            item.setChecked(true);
            return true;
        }
        //item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies(movies_order);
    }

    @Override
    public void onPause() {
        super.onPause();
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

         /*
         * Código para mudar a cor do toolbar
         *
         * ActionBar bar = getSupportActionBar();
         * bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BCAAA4")));
         */

        //ActionBar bar = getSupportActionBar();
        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9E9E9E")));


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
