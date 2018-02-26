package com.projects.jezinka.popularmovies.Model;

import org.json.JSONObject;

public class Poster {

    private String id;
    private String poster;

    public String getPoster() {
        return poster;
    }

    public String getId() {
        return id;
    }

    public Poster() {
    }

    public Poster(JSONObject movieJSON) {
        this.id = movieJSON.optString("id");
        this.poster = movieJSON.optString("poster_path");
    }
}
