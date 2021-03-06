package com.projects.jezinka.popularmovies.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.jezinka.popularmovies.BuildConfig;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.model.GenericList;
import com.projects.jezinka.popularmovies.model.MovieDetails;
import com.projects.jezinka.popularmovies.model.MovieReview;
import com.projects.jezinka.popularmovies.model.MovieVideo;
import com.projects.jezinka.popularmovies.service.TheMovieDbService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.BACKDROP;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.CONTENT_URI;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.ID;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.OVERVIEW;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.POSTER;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.RELEASE_DATE;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.TITLE;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.VOTE_AVERAGE;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_DETAILS = "MOVIE_DETAILS";
    private static final String TAG = "Details Activity";

    private static final int BUTTON_ON = android.R.drawable.btn_star_big_on;
    private static final int BUTTON_OFF = android.R.drawable.btn_star_big_off;

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static final String THUMBNAIL_YOUTUBE_URL = "http://img.youtube.com/vi/";
    private static final String DEFAULT_JPG = "/mqdefault.jpg";

    MovieDetails movieDetails;

    @BindView(R.id.plot_tv)
    TextView plotTextView;
    @BindView(R.id.realease_date_tv)
    TextView releaseDateTextView;
    @BindView(R.id.vote_tv)
    TextView voteAverageTextView;
    @BindView(R.id.poster)
    ImageView imageView;
    @BindView(R.id.favorites_btn)
    ImageButton favoritesButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.trailers_ll)
    LinearLayout trailersLayout;
    @BindView(R.id.reviews_ll)
    LinearLayout reviewsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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
        } else {
            movieDetails.setFavorite(getFavoriteButtonState());
        }

        populateUI();
        sendQueryForTrailers();
        sendQueryForReviews();
    }

    private void populateUI() {
        Picasso.with(this)
                .load(movieDetails.getBackdropPoster())
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.star_off)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);

        toolbar.setTitle(movieDetails.getTitle());
        plotTextView.setText(movieDetails.getOverview());
        releaseDateTextView.setText(movieDetails.getReleaseDate());
        voteAverageTextView.setText(String.valueOf(movieDetails.getVoteAverage()));
        favoritesButton.setImageResource(getFavoriteButtonImage());
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

    private void addFavorites() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, movieDetails.getId());
        contentValues.put(TITLE, movieDetails.getTitle());
        contentValues.put(POSTER, movieDetails.getPosterPath());
        contentValues.put(OVERVIEW, movieDetails.getOverview());
        contentValues.put(RELEASE_DATE, movieDetails.getReleaseDate());
        contentValues.put(VOTE_AVERAGE, movieDetails.getVoteAverage());
        contentValues.put(BACKDROP, movieDetails.getBackdropPath());

        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);
        if (uri != null) {
            favoritesButton.setImageResource(BUTTON_ON);
        }
    }

    private void removeFavorites() {
        Uri contentUri = ContentUris.withAppendedId(CONTENT_URI, Long.valueOf(movieDetails.getId()));
        int deletedRows = getContentResolver().delete(contentUri, null, null);

        if (deletedRows > 0) {
            favoritesButton.setImageResource(BUTTON_OFF);
        }
    }

    private boolean getFavoriteButtonState() {
        Uri contentUri = ContentUris.withAppendedId(CONTENT_URI, Long.valueOf(movieDetails.getId()));
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, TITLE);
        if (cursor != null) {
            Boolean isFavorite = cursor.getCount() > 0;
            cursor.close();
            return isFavorite;
        }
        return false;
    }

    private int getFavoriteButtonImage() {
        return movieDetails.isFavorite() ? BUTTON_ON : BUTTON_OFF;
    }

    private void sendQueryForTrailers() {
        final Context mContext = this;
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<GenericList<MovieVideo>> call = theMovieDbService.loadVideos(movieDetails.getId(), BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<GenericList<MovieVideo>>() {
            @Override
            public void onResponse(@NonNull Call<GenericList<MovieVideo>> call, @NonNull Response<GenericList<MovieVideo>> response) {

                GenericList<MovieVideo> body = response.body();
                MovieVideo[] results = body.getResults();
                Log.i("test", "Results length: " + String.valueOf(results.length));
                for (final MovieVideo video : results) {
                    final String key = video.getKey();
                    ImageButton trailersButton = new ImageButton(mContext);

                    Picasso.with(mContext)
                            .load(THUMBNAIL_YOUTUBE_URL + key + DEFAULT_JPG)
                            .placeholder(android.R.drawable.ic_media_play)
                            .error(android.R.drawable.stat_notify_error)
                            .into(trailersButton);

                    trailersButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String uri = YOUTUBE_BASE_URL + key;
                            Uri webpage = Uri.parse(uri);
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            mContext.startActivity(webIntent);
                        }
                    });

                    trailersLayout.addView(trailersButton);
                }
            }

            @Override
            public void onFailure
                    (@NonNull Call<GenericList<MovieVideo>> call, @NonNull Throwable t) {
                Log.i(TAG, getResources().getString(R.string.no_results));
                Toast.makeText(mContext, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendQueryForReviews() {
        final Context mContext = this;
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<GenericList<MovieReview>> call = theMovieDbService.loadReviews(movieDetails.getId(), BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<GenericList<MovieReview>>() {
            @Override
            public void onResponse(@NonNull Call<GenericList<MovieReview>> call, @NonNull Response<GenericList<MovieReview>> response) {

                GenericList<MovieReview> body = response.body();
                MovieReview[] results = body.getResults();
                for (MovieReview movieReview : results) {
                    LinearLayout reviewLayout = new LinearLayout(mContext);
                    reviewLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView authorTextView = new TextView(mContext);
                    authorTextView.setText(getResources().getString(R.string.author, movieReview.getAuthor()));

                    TextView contentTextView = new TextView(mContext);
                    contentTextView.setText(movieReview.getContent());

                    reviewLayout.addView(authorTextView);
                    reviewLayout.addView(contentTextView);
                    reviewLayout.addView(getSeparator());

                    reviewsLayout.addView(reviewLayout);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericList<MovieReview>> call, @NonNull Throwable t) {
                Log.i(TAG, getResources().getString(R.string.no_results));
                Toast.makeText(mContext, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @NonNull
    private View getSeparator() {
        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4));
        separator.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        return separator;
    }

    public void onFavoritesButtonClick(View view) {
        movieDetails.setFavorite(!movieDetails.isFavorite());

        if (movieDetails.isFavorite()) {
            addFavorites();
        } else {
            removeFavorites();
        }
    }
}
