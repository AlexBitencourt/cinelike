package com.example.android.cineliketrailer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexbitencourt on 23/06/17.
 */
public class MovieTrailer implements Parcelable {

    public static final String EXTRA_MOVIE_TRAILER = "movietrailers";

    /**
     * Nome do trailer do filme
     */
    public String trailer_name;

    /**
     * Chave do trailer do filme
     */
    public String trailer_key;

    /**
     * Chave do trailer do filme
     */
    public String trailer_id;

    /**
     * Thumb do trailer
     */
    private String youtube_thumb;


    /**
     * Contrutor do novo {@link MovieTrailer} objeto.
     *
     * @param trailerName é o nome do trailer do filme
     * @param trailerKey é o link para assistir o filme no youtube
     * @param trailerId é o Id do filme
     * @param youtubeThumb é o thumb do trailer no youtube
     */
    public MovieTrailer(String trailerName, String trailerKey, String trailerId,
                        String youtubeThumb) {
        this.trailer_name = trailerName;
        this.trailer_key = trailerKey;
        this.trailer_id = trailerId;
        this.youtube_thumb = youtubeThumb;
    }

    public MovieTrailer(Parcel in) {
        trailer_name = in.readString();
        trailer_key = in.readString();
        trailer_id = in.readString();
        youtube_thumb = in.readString();

    }

    /**
     * @return o nome do trailer
     */
    public String getTrailerName() {
        return trailer_name;
    }

    /**
     * @return a chave do trailer
     */
    public String getTrailerKey() {
        return trailer_key;
    }

    /**
     * @return o id do trailer
     */
    public String getTrailerId() {
        return trailer_id;
    }

    /**
     * @return o thumb do trailer no YouTube
     */
    public String getYoutube_thumb() {
        return youtube_thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailer_name);
        parcel.writeString(trailer_key);
        parcel.writeString(trailer_id);
        parcel.writeString(youtube_thumb);

    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

}
