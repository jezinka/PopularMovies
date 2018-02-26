package com.projects.jezinka.popularmovies.Model;

import org.json.JSONObject;

public class MovieDetails {
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

    public MovieDetails() {
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
}
