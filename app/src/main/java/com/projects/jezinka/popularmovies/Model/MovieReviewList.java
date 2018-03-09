package com.projects.jezinka.popularmovies.Model;

public class MovieReviewList {

    MovieReview[] results;

    public String[] getResults() {
        String[] reviews = new String[results.length];
        for (int i = 0; i < results.length; i++) {
            reviews[i] = results[i].content.substring(0, 100) + "\n\n" + results[i].url;
        }
        return reviews;
    }
}
