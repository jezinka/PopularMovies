package com.projects.jezinka.popularmovies.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.data.MovieDetailsDbHelper;
import com.projects.jezinka.popularmovies.model.MovieDetails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_DETAILS = "MOVIE_DETAILS";
    public static final String MOVIE_ID = "ID";
    private static final String TAG = "Details Activity";

    private static final int BUTTON_ON = android.R.drawable.btn_star_big_on;
    private static final int BUTTON_OFF = android.R.drawable.btn_star_big_off;

    MovieDetails movieDetails;
    SQLiteDatabase mDb;

    @BindView(R.id.title_tv)
    TextView titleTextView;
    @BindView(R.id.plot_tv)
    TextView plotTextView;
    @BindView(R.id.realease_date_tv)
    TextView releaseDateTextView;
    @BindView(R.id.vote_tv)
    TextView voteAverageTextView;
    @BindView(R.id.poster)
    ImageView imageView;
    @BindView(R.id.review_btn)
    Button reviewButton;
    @BindView(R.id.trailers_btn)
    Button trailersButton;
    @BindView(R.id.favorites_btn)
    ImageButton favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        MovieDetailsDbHelper helper = new MovieDetailsDbHelper(this);
        mDb = helper.getWritableDatabase();

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            movieDetails = savedInstanceState.getParcelable(MOVIE_DETAILS);
        } else {
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            }
            movieDetails = intent.getExtras().getParcelable(MOVIE_DETAILS);
        }

        if (movieDetails == null) {
            closeOnError();
        }

        Picasso.with(this)
                .load(movieDetails.getDetailPosterPath())
                .placeholder(android.R.drawable.star_off)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);

        titleTextView.setText(movieDetails.getTitle());
        plotTextView.setText(movieDetails.getOverview());
        releaseDateTextView.setText(movieDetails.getReleaseDate());
        voteAverageTextView.setText(String.valueOf(movieDetails.getVoteAverage()));
        favoritesButton.setImageResource(getFavoriteButtonResource(movieDetails.getId()));

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent intent = new Intent(context, ReviewsActivity.class);
                intent.putExtra(MOVIE_ID, movieDetails.getId());
                context.startActivity(intent);
            }
        });

        trailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent intent = new Intent(context, TrailersActivity.class);
                intent.putExtra(MOVIE_ID, movieDetails.getId());
                context.startActivity(intent);
            }
        });

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieDetails.setFavorite(!movieDetails.isFavorite());

                if (movieDetails.isFavorite()) {
                    favoritesButton.setImageResource(BUTTON_ON);
                    addFavorites(movieDetails);
                } else {
                    favoritesButton.setImageResource(BUTTON_OFF);
                    removeFavorites(movieDetails.getId());
                }
            }
        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie not available", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(MOVIE_DETAILS, movieDetails);
    }

    private void addFavorites(MovieDetails movieDetails) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDetailsEntry.ID, movieDetails.getId());
        contentValues.put(MovieDetailsEntry.TITLE, movieDetails.getTitle());
        contentValues.put(MovieDetailsEntry.POSTER, movieDetails.getPosterPath());
        contentValues.put(MovieDetailsEntry.OVERVIEW, movieDetails.getOverview());
        contentValues.put(MovieDetailsEntry.RELEASE_DATE, movieDetails.getReleaseDate());
        contentValues.put(MovieDetailsEntry.VOTE_AVERAGE, movieDetails.getVoteAverage());

        mDb.insert(
                MovieDetailsEntry.TABLE_NAME,
                null,
                contentValues
        );
    }

    private void removeFavorites(final String movieId) {
        mDb.delete(MovieDetailsEntry.TABLE_NAME,
                MovieDetailsEntry.ID + "=?",
                new String[]{movieId});
    }

    private int getFavoriteButtonResource(String movieId) {

        String sql = "SELECT " + MovieDetailsEntry.ID +
                " FROM " + MovieDetailsEntry.TABLE_NAME +
                " WHERE " + MovieDetailsEntry.ID + "=" + movieId;

        Cursor cursor = mDb.rawQuery(sql, null);
        Boolean isFavorite = cursor.getCount() > 0;
        cursor.close();

        return isFavorite ? BUTTON_ON : BUTTON_OFF;
    }
}
