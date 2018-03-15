package com.projects.jezinka.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDetailsContract {

    public static final String AUTHORITY = "com.projects.jezinka.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIE_DETAILS = "movie_details";

    public static final class MovieDetailsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_DETAILS).build();

        public static final String TABLE_NAME = "movie_details";

        public static final String ID = "ID";
        public static final String POSTER = "POSTER";
        public static final String TITLE = "TITLE";
        public static final String OVERVIEW = "OVERVIEW";
        public static final String VOTE_AVERAGE = "VOTE_AVERAGE";
        public static final String RELEASE_DATE = "RELEASE_DATE";
    }
}
