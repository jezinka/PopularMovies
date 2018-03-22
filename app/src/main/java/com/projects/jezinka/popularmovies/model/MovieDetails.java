package com.projects.jezinka.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry;

public class MovieDetails implements Parcelable {
    private static final String posterBasePath = "https://image.tmdb.org/t/p/";

    private String id;
    private String title;
    private String release_date;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private Double vote_average;
    private boolean isFavorite;
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getBackdropPoster() {
        System.out.println(backdrop_path);
        return posterBasePath + "w500" + backdrop_path;
    }

    public String getListPosterPath() {
        return posterBasePath + "w185/" + poster_path;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public String getId() {
        return id;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public MovieDetails(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
    }

    public MovieDetails(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndex(MovieDetailsEntry.ID));
        this.title = cursor.getString(cursor.getColumnIndex(MovieDetailsEntry.TITLE));
        this.release_date = cursor.getString(cursor.getColumnIndex(MovieDetailsEntry.RELEASE_DATE));
        this.overview = cursor.getString(cursor.getColumnIndex(MovieDetailsEntry.OVERVIEW));
        this.vote_average = cursor.getDouble(cursor.getColumnIndex(MovieDetailsEntry.VOTE_AVERAGE));
        this.poster_path = cursor.getString(cursor.getColumnIndex(MovieDetailsEntry.POSTER));
        this.backdrop_path = cursor.getString(cursor.getColumnIndex(MovieDetailsEntry.BACKDROP));
        this.isFavorite = true;
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", vote_average=" + vote_average +
                ", overview='" + overview + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeDouble(vote_average);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
    }

    public static final Parcelable.Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel parcel) {
            return new MovieDetails(parcel);
        }

        @Override
        public MovieDetails[] newArray(int i) {
            return new MovieDetails[0];
        }
    };

}
