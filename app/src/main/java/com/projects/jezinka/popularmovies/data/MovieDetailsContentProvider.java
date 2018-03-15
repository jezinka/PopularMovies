package com.projects.jezinka.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.ID;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.TABLE_NAME;


public class MovieDetailsContentProvider extends ContentProvider {

    private MovieDetailsDbHelper dbHelper;

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
        Context context = getContext();
        dbHelper = new MovieDetailsDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;
        switch (match) {
            case MOVIES:
                returnCursor = db.query(
                        MovieDetailsContract.MovieDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                returnCursor = db.query(
                        TABLE_NAME,
                        projection,
                        ID + "=?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieDetailsContract.MovieDetailsEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + TABLE_NAME);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] queryArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int deletedRows;

        switch (match) {
            case MOVIE_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                deletedRows = db.delete(TABLE_NAME, ID + "=?", new String[]{movieId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
