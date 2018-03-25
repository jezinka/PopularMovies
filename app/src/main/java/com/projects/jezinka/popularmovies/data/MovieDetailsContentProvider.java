package com.projects.jezinka.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry;
import com.projects.jezinka.popularmovies.model.MovieDetails;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.BACKDROP;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.ID;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.OVERVIEW;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.POSTER;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.RELEASE_DATE;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.TITLE;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.VOTE_AVERAGE;


public class MovieDetailsContentProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieDetailsContract.AUTHORITY, MovieDetailsContract.PATH_MOVIE_DETAILS, MOVIES); //all movie details
        uriMatcher.addURI(MovieDetailsContract.AUTHORITY, MovieDetailsContract.PATH_MOVIE_DETAILS + "/#", MOVIE_WITH_ID); //by ID

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Realm.init(getContext());
        new RealmConfiguration.Builder().build();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String[] movieDetailsColumns = new String[]{ID, TITLE, POSTER, BACKDROP, OVERVIEW, RELEASE_DATE, VOTE_AVERAGE};
        int match = sUriMatcher.match(uri);

        MatrixCursor returnCursor = new MatrixCursor(movieDetailsColumns);
        try (Realm realm = Realm.getDefaultInstance()) {
            switch (match) {
                case MOVIES:
                    RealmResults<MovieDetails> allMovies = realm.where(MovieDetails.class).findAll().sort(sortOrder != null ? sortOrder : "id");
                    for (MovieDetails movieDetails : allMovies) {
                        Object[] rowData = new Object[]{movieDetails.getId(), movieDetails.getTitle(), movieDetails.getPosterPath(), movieDetails.getBackdropPath(), movieDetails.getOverview(), movieDetails.getReleaseDate(), movieDetails.getVoteAverage()};
                        returnCursor.addRow(rowData);
                    }
                    break;

                case MOVIE_WITH_ID:
                    String movieId = uri.getPathSegments().get(1);
                    MovieDetails movieDetails = realm.where(MovieDetails.class).equalTo("id", movieId).findFirst();
                    if (movieDetails != null) {
                        Object[] rowData = new Object[]{movieDetails.getId(), movieDetails.getTitle(), movieDetails.getPosterPath(), movieDetails.getBackdropPath(), movieDetails.getOverview(), movieDetails.getReleaseDate(), movieDetails.getVoteAverage()};
                        returnCursor.addRow(rowData);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable final ContentValues contentValues) {

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        try (Realm realm = Realm.getDefaultInstance()) {
            switch (match) {
                case MOVIES:

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            MovieDetails movieDetails = realm.createObject(MovieDetails.class, contentValues.getAsString(ID));
                            movieDetails.setTitle(contentValues.get(TITLE).toString());
                            movieDetails.setPoster_path(contentValues.getAsString(POSTER));
                            movieDetails.setBackdrop_path(contentValues.getAsString(BACKDROP));
                            movieDetails.setOverview(contentValues.getAsString(OVERVIEW));
                            movieDetails.setVote_average(contentValues.getAsDouble(VOTE_AVERAGE));
                            movieDetails.setRelease_date(contentValues.getAsString(RELEASE_DATE));
                            movieDetails.setFavorite(true);
                        }
                    });

                    returnUri = ContentUris.withAppendedId(MovieDetailsEntry.CONTENT_URI, 1);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] queryArgs) {
        int match = sUriMatcher.match(uri);

        int deletedRows = 0;

        try (Realm realm = Realm.getDefaultInstance()) {
            switch (match) {
                case MOVIE_WITH_ID:
                    String movieId = uri.getPathSegments().get(1);
                    MovieDetails movieDetails = realm.where(MovieDetails.class).equalTo("id", movieId).findFirst();
                    realm.beginTransaction();
                    movieDetails.deleteFromRealm();
                    deletedRows++;
                    realm.commitTransaction();
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
