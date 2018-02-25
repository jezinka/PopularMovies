package com.projects.jezinka.popularmovies;

import org.json.JSONObject;

public class MovieDetails {
    private String title;
    private String releaseDate;
    private String poster;
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

    private String plotSynopsis;

    public MovieDetails() {
    }

    public MovieDetails(JSONObject movieJSON) {
        this.title = movieJSON.optString("title");
        this.releaseDate = movieJSON.optString("release_date");
        this.plotSynopsis = movieJSON.optString("overview");
        this.voteAverage = movieJSON.optDouble("vote_average");
        this.poster = movieJSON.optString("poster_path");
    }
}
