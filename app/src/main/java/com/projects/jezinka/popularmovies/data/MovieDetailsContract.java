package com.projects.jezinka.popularmovies.data;

import android.provider.BaseColumns;

public class MovieDetailsContract {

    public static final class MovieDetailsEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie_details";

        public static final String MOVIE_ID_COLUMN = "ID";
        public static final String MOVIE_POSTER_COLUMN = "POSTER";
        public static final String MOVIE_TITLE_COLUMN = "TITLE";
        public static final String MOVIE_OVERVIEW_COLUMN = "OVERVIEW";
        public static final String MOVIE_VOTE_AVERAGE_COLUMN = "VOTE_AVERAGE";
        public static final String MOVIE_RELEASE_DATE_COLUMN = "RELEASE_DATE";
    }
}
