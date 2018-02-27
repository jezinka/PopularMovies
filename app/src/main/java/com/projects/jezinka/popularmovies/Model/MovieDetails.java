package com.projects.jezinka.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieDetails implements Parcelable {

    private String id;
    private String title;
    private String releaseDate;
    private String poster;
    private String plotSynopsis;
    private Double voteAverage;

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getId() {
        return id;
    }

    public MovieDetails(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.plotSynopsis = in.readString();
        this.voteAverage = in.readDouble();
        this.poster = in.readString();
    }

    public MovieDetails(JSONObject movieJSON) {
        this.id = movieJSON.optString("id");
        this.title = movieJSON.optString("title");
        this.releaseDate = movieJSON.optString("release_date");
        this.plotSynopsis = movieJSON.optString("overview");
        this.voteAverage = movieJSON.optDouble("vote_average");
        this.poster = movieJSON.optString("poster_path");
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", poster='" + poster + '\'' +
                ", voteAverage=" + voteAverage +
                ", plotSynopsis='" + plotSynopsis + '\'' +
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
        parcel.writeString(releaseDate);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(voteAverage);
        parcel.writeString(poster);
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
