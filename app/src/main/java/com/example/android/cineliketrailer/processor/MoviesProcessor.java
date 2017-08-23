package com.example.android.cineliketrailer.processor;

import android.text.TextUtils;

import com.example.android.cineliketrailer.model.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhiegoabrantes on 25/05/17.
 */

public class MoviesProcessor {

    public static ArrayList<MovieDetails> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        ArrayList<MovieDetails> movieDetalsArrayList = new ArrayList<>();

        // Se a cadeia JSON estiver vazia ou nula, volte mais cedo.
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }

        final String MV_RESULTS = "results";
        final String MV_ORIGINAL_TITLE = "title";
        final String MV_VOTE_AVERAGE = "vote_average";
        final String MV_POSTER_PATH = "poster_path";
        final String MV_OVERVIEW = "overview";
        final String MV_RELEASE_DATE = "release_date";
        final String MV_BACKDROP_PATH = "backdrop_path";
        final String MV_ID = "id";
        final String MV_LANGUAGE = "original_language";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);

        String baseURL = "http://image.tmdb.org/t/p/w500/";

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);

            String title = currentMovie.optString(MV_ORIGINAL_TITLE);
            String vote_average = currentMovie.optString(MV_VOTE_AVERAGE);
            String posterURL = currentMovie.optString(MV_POSTER_PATH);
            String moviePoster = baseURL + posterURL;
            String overView = currentMovie.optString(MV_OVERVIEW);
            String releaseDate = currentMovie.optString(MV_RELEASE_DATE);
            String backdropURL = currentMovie.optString(MV_BACKDROP_PATH);
            String movieBackdrop = baseURL + backdropURL;
            String movieId = currentMovie.optString(MV_ID);
            String movieLanguage = currentMovie.optString(MV_LANGUAGE);

            movieDetalsArrayList.add(new MovieDetails(title, vote_average, moviePoster, overView,
                    releaseDate, movieBackdrop, movieId, movieLanguage));
        }

        return movieDetalsArrayList;
    }

}
