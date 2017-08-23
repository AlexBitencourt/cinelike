package com.example.android.cineliketrailer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.cineliketrailer.R;
import com.example.android.cineliketrailer.activities.DetailActivity;
import com.example.android.cineliketrailer.data.MovieContract;
import com.example.android.cineliketrailer.model.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbitencourt on 09/08/17.
 */

public class FavoriteAdapter extends CursorAdapter {

    private static final String LOG_TAG = FavoriteAdapter.class.getSimpleName();
    private Context mContext;

    private FloatingActionButton mFab;

    public FavoriteAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.gridview_movie, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        mFab = (FloatingActionButton) view.findViewById(R.id.button_favorite);

        TextView titleText = (TextView) view.findViewById(R.id.title_textview);
        int title = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_FAVORITE_TITLE);
        String titleMovie = cursor.getString(title);
        titleText.setText(titleMovie);

        ImageView imageMovie = (ImageView) view.findViewById(R.id.movie_poster);
        final int posterIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH);
        final String posterMovie = cursor.getString(posterIndex);
        Picasso.with(mContext).load(posterMovie).into(imageMovie);

        Picasso.with(mContext)
                .load(posterMovie)
                .placeholder(R.drawable.ic_photo_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .resize(6000, 2000)
                .onlyScaleDown()
                .into(imageMovie);

        final int position = cursor.getPosition();

        imageMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * Marca a posição do filme selecionado como favorito para que cada filme fique guardado
                 * na sua posição. Sem esta linha de código, os filmes salvos sempre aparecem com os detalhes
                 * do primeiro filme selecionado como favorito.
                 */
                cursor.moveToPosition(position);

                int title = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE);
                int vote = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE);
                int poster = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER_PATH);
                int overview = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_OVERVIEW);
                int release = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE);
                int backdrop = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_BACKDROP_PATH);
                int id = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_ID);
                int language = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_LANGUAGE);

                final String movieTitle = cursor.getString(title);
                final String movieVote = cursor.getString(vote);
                final String moviePoster = cursor.getString(poster);
                final String movieOverview = cursor.getString(overview);
                final String movieRelease = cursor.getString(release);
                final String movieBackdrop = cursor.getString(backdrop);
                final String movieId = cursor.getString(id);
                final String movieLanguage = cursor.getString(language);

                MovieDetails movie = new MovieDetails(movieTitle, movieVote, moviePoster, movieOverview,
                        movieRelease, movieBackdrop, movieId, movieLanguage);

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(MovieDetails.EXTRA_MOVIE_DETAILS, movie);
                context.startActivity(intent);

            }
        });

    }
}