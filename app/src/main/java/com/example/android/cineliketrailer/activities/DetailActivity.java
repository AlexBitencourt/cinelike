package com.example.android.cineliketrailer.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cineliketrailer.adapter.ReviewAdapter;
import com.example.android.cineliketrailer.adapter.TrailerAdapter;
import com.example.android.cineliketrailer.model.MovieDetails;
import com.example.android.cineliketrailer.model.MovieReview;
import com.example.android.cineliketrailer.model.MovieTrailer;
import com.example.android.cineliketrailer.R;
import com.example.android.cineliketrailer.adapter.CustomMovieAdapter;
import com.example.android.cineliketrailer.async.AsyncTaskDelegatorReview;
import com.example.android.cineliketrailer.async.AsyncTaskDelegatorTrailer;
import com.example.android.cineliketrailer.async.FetchReviewTask;
import com.example.android.cineliketrailer.async.FetchTrailersTask;
import com.example.android.cineliketrailer.data.MovieContract;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbitencourt on 04/05/17.
 */
public class DetailActivity extends AppCompatActivity implements AsyncTaskDelegatorTrailer, AsyncTaskDelegatorReview,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    CustomMovieAdapter customMovieAdapter;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private RecyclerView mRecyclerTrailers;
    private RecyclerView mRecyclerReviews;
    private String mMovieStr;
    private FloatingActionButton mFab;
    Long movieId;
    private MovieDetails currentMovie;

    private static final String MOVIE_SHARE_HASHTAG = " #CinelikeApp";

    private boolean isFavorite = false;
    private boolean statusDelete = false;
    private boolean statusSave = false;

    ArrayList<MovieTrailer> movieTrailer = new ArrayList<>();
    ArrayList<MovieReview> movieReviews = new ArrayList<>();

    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry._ID,
            MovieContract.MoviesEntry.COLUMN_MOVIE_ID,
            MovieContract.MoviesEntry.COLUMN_TITLE,
            MovieContract.MoviesEntry.COLUMN_OVERVIEW,
            MovieContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MovieContract.MoviesEntry.COLUMN_POSTER_PATH,
            MovieContract.MoviesEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MoviesEntry.COLUMN_LANGUAGE,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_details);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        currentMovie = intent.getParcelableExtra(MovieDetails.EXTRA_MOVIE_DETAILS);
        currentMovie.getId();

        /*
         * List Details
         */
        TextView titleDetails = (TextView) findViewById(R.id.title_details);
        TextView releaseDateDetails = (TextView) findViewById(R.id.release_date_details);
        TextView overviewDetails = (TextView) findViewById(R.id.overview_details);
        ImageView backdropDetails = (ImageView) findViewById(R.id.backdrop_details);
        TextView voteMovie = (TextView) findViewById(R.id.vote_details);
        ImageView posterDetails = (ImageView) findViewById(R.id.poster_details);
        TextView languageMovie = (TextView) findViewById(R.id.language_details);

        titleDetails.setText(currentMovie.getTitle());
        releaseDateDetails.setText(currentMovie.getRelease_date().substring(0, 4));
        overviewDetails.setText(currentMovie.getOverView());
        backdropDetails.setVisibility(View.VISIBLE);
        Picasso.with(this).load(currentMovie.getBackdrop_path()).into(backdropDetails);
        voteMovie.setText(currentMovie.getVote());
        posterDetails.setVisibility(View.VISIBLE);
        Picasso.with(this).load(currentMovie.getPosterUrl()).into(posterDetails);
        languageMovie.setText(currentMovie.getLanguage());


        // A atividade detalhada chamada via intenção. Inspecione a tentativa de dados do filme.
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) findViewById(R.id.title_details))
                    .setText(mMovieStr);
        }

        /*
         * RecyclerView Trailers
         */
        LinearLayoutManager lmTrailers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerTrailers = (RecyclerView) findViewById(R.id.recycler_trailers);
        mRecyclerTrailers.setLayoutManager(lmTrailers);

        trailerAdapter = new TrailerAdapter(movieTrailer, getApplicationContext());
        mRecyclerTrailers.setAdapter(trailerAdapter);

        /*
         * RecyclerView Reviews
         */
        LinearLayoutManager lmReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerReviews = (RecyclerView) findViewById(R.id.recycler_reviews);
        mRecyclerReviews.setLayoutManager(lmReviews);

        reviewAdapter = new ReviewAdapter(movieReviews, getApplicationContext());
        mRecyclerReviews.setAdapter(reviewAdapter);

        mRecyclerTrailers.setNestedScrollingEnabled(true);
        mRecyclerTrailers.setHasFixedSize(true);
        mRecyclerReviews.setNestedScrollingEnabled(true);
        mRecyclerReviews.setHasFixedSize(true);
        mRecyclerTrailers.setFocusable(false);

        /*
         * Update Task Trailers e Reviews
         */
        updateTrailerMovies(currentMovie.getId());
        updateReviewMovies(currentMovie.getId());

        isFavorite = isFavorite();

        /*
         * Button Favorite
         */
        mFab = (FloatingActionButton) findViewById(R.id.button_favorite);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite) {
                    mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                    isFavorite = true;
                    statusDelete = false;
                    statusSave = true;
                } else {
                    mFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                    isFavorite = false;
                    statusDelete = true;
                    statusSave = false;
                }
            }
        });

        if(isFavorite()) {
            mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(statusDelete) {
            deleteFavorite();
        } else if(statusSave){
            saveFavorite();
        }
    }

    public boolean isFavorite() {
        Cursor favoriteCursor = getApplicationContext().getContentResolver().query(
                MovieContract.FavoriteEntry.CONTENT_URI,
                new String[]{MovieContract.FavoriteEntry.COLUMN_FAVORITE_TITLE},
                MovieContract.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID + " = " + movieId,
                null,
                null);

        while (favoriteCursor.moveToNext()) {
            if(currentMovie.getTitle().equals(favoriteCursor.getString(
                    favoriteCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_FAVORITE_TITLE)))){
                isFavorite = true;
            }
        } return isFavorite;
    }

    private void updateTrailerMovies(String id) {
        FetchTrailersTask getTask = new FetchTrailersTask(this);
        getTask.execute(id);
    }

    private void updateReviewMovies(String id) {
        FetchReviewTask getTask = new FetchReviewTask(this);
        getTask.execute(id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                //MovieContract.MoviesEntry.CONTENT_URI,
                MovieContract.MoviesEntry.buildMoviesUri(id),
                DETAIL_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<MovieDetails> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            MovieDetails movie = new MovieDetails(
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_BACKDROP_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_LANGUAGE)));

            movies.add(movie);

        }
        cursor.close();

        // Update
        customMovieAdapter.updateData((ArrayList<MovieDetails>) movies);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void processFinishTrailer(ArrayList<MovieTrailer> result) {
        this.movieTrailer = result;
        trailerAdapter.setTrailers(movieTrailer);
    }


    @Override
    public void processFinishReview(ArrayList<MovieReview> result) {
        this.movieReviews = result;
        reviewAdapter.setReviews(movieReviews);

    }


    public void saveFavorite() {

        String id = currentMovie.getId();
        String title = currentMovie.getTitle();
        String overview = currentMovie.getOverView();
        String backdrop = currentMovie.getBackdrop_path();
        String vote = currentMovie.getVote();
        String poster = currentMovie.getPosterUrl();
        String release_date = currentMovie.getRelease_date();
        String language = currentMovie.getLanguage();

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ID, id);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_BACKDROP_PATH, backdrop);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE, vote);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_POSTER_PATH, poster);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, release_date);
        contentValues.put(MovieContract.MoviesEntry.COLUMN_LANGUAGE, language);

        try {
            Uri newUri = getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, contentValues);
            Log.v(LOG_TAG, "Uri: " + newUri.toString());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error newUri insert", e);
        }
    }

    private void deleteFavorite() {
        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI;
        int currentMovieId = Integer.valueOf(currentMovie.getId());
        if (uri != null) {
            int rowsDeleted = getContentResolver().delete(MovieContract.FavoriteEntry
                    .buildFavoriteUri(currentMovieId), null, null);
            if (rowsDeleted == 0) {
                //Toast.makeText(this, "Erro delete", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    /*
     * Código para item de compartilhamento nas redes sociais, e-mails...
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar o menu; Isso adiciona itens à barra de ação se estiver presente.
        getMenuInflater().inflate(R.menu.detailfragment, menu);

        // Recuperar o item do menu compartilhamento
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Obtenha o fornecedor e segure-o para definir / alterar a intenção de compartilhamento.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Anexe uma intenção a este ShareActionProvider. Você pode atualizar isso a qualquer momento,
        // como quando o usuário seleciona uma nova peça de dados que eles podem gostar de compartilhar.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
        return true;
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        // Esta flag evita que o app te leve para outro app quando tiver que voltar para ele mais tarde.
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Imforma que vamos compartilhar texto simpples.
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieStr + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }
}

