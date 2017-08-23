package com.example.android.cineliketrailer.processor;

import android.text.TextUtils;
import com.example.android.cineliketrailer.model.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexbitencourt on 24/06/17.
 */
public class TrailersProcessor {

    public static ArrayList<MovieTrailer> getTrailerDataFromJson(String movieJsonStr)
            throws JSONException {

        ArrayList<MovieTrailer> movieTrailersArrayList = new ArrayList<>();

        // Se a cadeia JSON estiver vazia ou nula, volte mais cedo.
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }

        final String MV_RESULTS = "results";
        final String MV_TRAILER_NAME = "name";
        final String MV_TRAILER_KEY = "key";
        final String MV_ID = "id";
        final String MV_YOUTUBE_THUMB_URL = "http://img.youtube.com/vi/";
        final String MV_YOUTUBE_THUMB_JPG = "/0.jpg";


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);

            String nameTrailer = currentMovie.optString(MV_TRAILER_NAME);
            String trailerKey = currentMovie.optString(MV_TRAILER_KEY);
            String trailerId = currentMovie.optString(MV_ID);
            String youtubeThumb = MV_YOUTUBE_THUMB_URL + currentMovie.optString(MV_TRAILER_KEY) + MV_YOUTUBE_THUMB_JPG;

            movieTrailersArrayList.add(new MovieTrailer(nameTrailer, trailerKey, trailerId,
                    youtubeThumb));
        }

        return movieTrailersArrayList;
    }
}
