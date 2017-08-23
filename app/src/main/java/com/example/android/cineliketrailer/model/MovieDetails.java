package com.example.android.cineliketrailer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexbitencourt on 07/05/17.
 */
public class MovieDetails implements Parcelable {

    public static final String EXTRA_MOVIE_DETAILS = "moviedetails";

    /**
     * Título do filme
     */
    public String title;

    /**
     * Avaliação do filme
     */
    public String vote;

    /**
     * Pôster do filme
     */
    public String posterUrl;

    /**
     * Overview do filme
     */
    public String overview;

    /**
     * Data do filme
     */
    public String release_date;

    /**
     * Backdrop do filme
     */
    public String backdrop_path;

    /**
     * Id de referência do filme
     */
    public String id;

    /**
     * Id de referência da língua de origem
     */
    public String language;


    /**
     * Contrutor do novo {@link MovieDetails} objeto.
     *
     * @param
     * @param originalTitle é o título do filme
     * @param vote_average  é a avaliação dada pelos usuários
     * @param posterPath    é a url do pôster
     * @param over          é a sinopse do pôster
     * @param date          é o ano de lançamento do pôster
     * @param backdrop      é uma foto de divilgação do pôster
     * @param movieId       é o id de referência do filme
     * @param languageMovie é a língua de origem do filme
     */
    public MovieDetails(String originalTitle, String vote_average, String posterPath, String over,
                        String date, String backdrop, String movieId, String languageMovie) {
        this.title = originalTitle;
        this.vote = vote_average;
        this.posterUrl = posterPath;
        this.overview = over;
        this.release_date = date;
        this.backdrop_path = backdrop;
        this.id = movieId;
        this.language = languageMovie;
    }

    protected MovieDetails(Parcel in) {
        title = in.readString();
        vote = in.readString();
        posterUrl = in.readString();
        overview = in.readString();
        release_date = in.readString();
        backdrop_path = in.readString();
        id = in.readString();
        language = in.readString();
    }

    /**
     * @return o título
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return a avaliação
     */
    public String getVote() {
        return vote;
    }

    /**
     * @return a url do pôster
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * @return o overview do filme
     */
    public String getOverView() {
        return overview;
    }

    /**
     * @return o overview do filme
     */
    public String getRelease_date() {
        return release_date;
    }

    /**
     * @return o backdrop do filme
     */
    public String getBackdrop_path() {
        return backdrop_path;
    }

    /**
     * @return o id do filme
     */
    public String getId() {
        return id;
    }

    /**
     * @return o id do língua de origem
     */
    public String getLanguage() {
        return language;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(vote);
        parcel.writeString(posterUrl);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(backdrop_path);
        parcel.writeString(id);
        parcel.writeString(language);
    }


    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };


}

