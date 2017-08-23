package com.example.android.cineliketrailer.processor;

import android.text.TextUtils;

import com.example.android.cineliketrailer.model.MovieReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexbitencourt on 10/07/17.
 */
public class ReviewsProcessor {

    public static ArrayList<MovieReview> getReviewDataFromJson(String movieJsonStr)
            throws JSONException {

        ArrayList<MovieReview> movieReviewArrayList = new ArrayList<>();

        // Se a cadeia JSON estiver vazia ou nula, volte mais cedo.
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }

        final String MV_RESULTS = "results";
        final String MV_REVIEW_AUTHOR = "author";
        final String MV_REVIEW_CONTENT = "content";
        final String MV_ID = "id";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);

            String reviewAuthor = currentMovie.optString(MV_REVIEW_AUTHOR);
            String reviewContent = currentMovie.optString(MV_REVIEW_CONTENT);
            String reviewId = currentMovie.optString(MV_ID);

            movieReviewArrayList.add(new MovieReview(reviewAuthor, reviewContent, reviewId));
        }
        return movieReviewArrayList;
    }
}
