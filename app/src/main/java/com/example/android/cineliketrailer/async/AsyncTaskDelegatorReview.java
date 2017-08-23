package com.example.android.cineliketrailer.async;

import com.example.android.cineliketrailer.model.MovieReview;

import java.util.ArrayList;

/**
 * Created by alexbitencourt on 10/07/17.
 */
public interface AsyncTaskDelegatorReview {
    void processFinishReview(ArrayList<MovieReview> result);

}
