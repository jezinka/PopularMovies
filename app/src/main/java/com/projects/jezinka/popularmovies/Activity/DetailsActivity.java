package com.projects.jezinka.popularmovies.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.jezinka.popularmovies.Model.MovieDetails;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";

    public static final String ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        String id = intent.getStringExtra(ID);
        if (id == null) {
            closeOnError();
            return;
        }
        new FetchMovieDetailTask(this).execute(id);

    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie not available", Toast.LENGTH_SHORT).show();
    }

    public class FetchMovieDetailTask extends AsyncTask<String, Void, MovieDetails> {

        Context mContext;

        public FetchMovieDetailTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected MovieDetails doInBackground(String... params) {

            URL movieRequestUrl = NetworkUtils.buildUrl(params[0]);

            try {
                String movie = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                JSONObject movieJSON = new JSONObject(movie);
                return new MovieDetails(movieJSON);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetails movieDetail) {
            if (movieDetail != null) {
                ImageView imageView = findViewById(R.id.poster);
                Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342" + movieDetail.getPoster()).into(imageView);

                TextView titleTextView = findViewById(R.id.title_tv);
                titleTextView.setText(movieDetail.getTitle());

                TextView plotTextView = findViewById(R.id.plot_tv);
                plotTextView.setText(movieDetail.getPlotSynopsis());

                TextView releaseDateTextView = findViewById(R.id.realease_date_tv);
                releaseDateTextView.setText(movieDetail.getReleaseDate());

                TextView voteAverageTextView = findViewById(R.id.vote_tv);
                voteAverageTextView.setText(String.valueOf(movieDetail.getVoteAverage()));
            } else {
                Log.i(TAG, "The query returns no results");
            }
        }
    }
}
