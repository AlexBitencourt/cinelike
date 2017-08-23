package com.example.android.cineliketrailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cineliketrailer.model.MovieDetails;
import com.example.android.cineliketrailer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexbitencourt on 07/05/17.
 */
public class CustomMovieAdapter extends ArrayAdapter<MovieDetails> {

    private final String LOG_TAG = CustomMovieAdapter.class.getSimpleName();

    List<MovieDetails> movieDetails;

    /**
     * Contrutor do novo {@link CustomMovieAdapter}
     *
     * @param context do app.
     * @param movies  é a lista de filmes, que é a fonte de dados do adapter.
     */

    public CustomMovieAdapter(Context context, List<MovieDetails> movies) {
        super(context, 0, movies);
    }

    public void updateData(ArrayList<MovieDetails> newPosters) {
        this.movieDetails = newPosters;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (movieDetails != null) {
            return movieDetails.size();
        } else {
            return 0;
        }
    }

    @Override
    public MovieDetails getItem(int position) {
        return movieDetails.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Verifica se há uma exibição de item de lista existente que possamos reutilizar,
        // caso contrário, se a convertView for nulo, então infla um novo layout de item de lista.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.gridview_movie, parent, false);
        }

        // Acha o título do filme na posição dada na lista de filmes.
        MovieDetails currentMovie = getItem(position);

        TextView titleMovie = (TextView) listItemView.findViewById(R.id.title_textview);
        titleMovie.setText(currentMovie.getTitle());

        ImageView posterMovie = (ImageView) listItemView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(currentMovie.getPosterUrl()).into(posterMovie);

        Picasso.with(getContext())
                .load(currentMovie.getPosterUrl())
                .placeholder(R.drawable.ic_photo_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .resize(6000, 2000)
                .onlyScaleDown()
                .into(posterMovie);

        return listItemView;
    }

}
