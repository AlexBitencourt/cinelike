package com.example.android.cineliketrailer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexbitencourt on 10/07/17.
 */
public class MovieReview implements Parcelable{

    public static final String EXTRA_MOVIE_REVIEWS= "moviereviews";

    /**
     * Nome do autor do review
     */
    public String review_author;

    /**
     * Conteúdo do review
     */
    public String review_content;

    /**
     * Id do review
     */
    public String review_id;


    /**
     * Contrutor do novo {@link MovieTrailer} objeto.
     *
     * @param reviewAuthor é o nome do autor do review
     * @param reviewContent é o conteúdo do review
     * @param reviewId é o id do review
     */
    public MovieReview(String reviewAuthor, String reviewContent, String reviewId) {
        this.review_author = reviewAuthor;
        this.review_content = reviewContent;
        this.review_id = reviewId;
    }


    public MovieReview(Parcel in) {
        review_author = in.readString();
        review_content = in.readString();
        review_id = in.readString();

    }

    /**
     * @return o nome do autor
     */
    public String getReviewAuthor() {
        return review_author;
    }

    /**
     * @return o nome do autor
     */
    public String getReviewContent() {
        return review_content;
    }

    /**
     * @return o nome do autor
     */
    public String getReviewId() {
        return review_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(review_author);
        parcel.writeString(review_content);
        parcel.writeString(review_id);
    }


    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

}
