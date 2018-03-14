package com.projects.jezinka.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry;


public class MovieDetailsDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "waitlist.db";
    static final int DATABASE_VERSION = 2;

    public MovieDetailsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + MovieDetailsEntry.TABLE_NAME + "(" +
                MovieDetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieDetailsEntry.ID + " INTEGER, " +
                MovieDetailsEntry.TITLE + " TEXT NOT NULL, " +
                MovieDetailsEntry.POSTER + " TEXT NOT NULL, " +
                MovieDetailsEntry.OVERVIEW + " TEXT NOT NULL, " +
                MovieDetailsEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                MovieDetailsEntry.VOTE_AVERAGE + " REAL NOT NULL" +
                ")";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDetailsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
