package com.projects.jezinka.popularmovies.data;

import android.provider.BaseColumns;

public class MovieDetailsContract {

    public static final class MovieDetailsEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie_details";

        public static final String ID = "ID";
        public static final String POSTER = "POSTER";
        public static final String TITLE = "TITLE";
        public static final String OVERVIEW = "OVERVIEW";
        public static final String VOTE_AVERAGE = "VOTE_AVERAGE";
        public static final String RELEASE_DATE = "RELEASE_DATE";
    }
}
