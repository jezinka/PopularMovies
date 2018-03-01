package com.projects.jezinka.popularmovies.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.jezinka.popularmovies.Model.MovieDetails;
import com.projects.jezinka.popularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_DETAILS = "MOVIE_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        MovieDetails movieDetail = intent.getExtras().getParcelable(MOVIE_DETAILS);

        if (movieDetail == null) {
            closeOnError();
        }

        ImageView imageView = findViewById(R.id.poster);
        Picasso.with(this).load(movieDetail.getDetailPosterPath()).into(imageView);

        TextView titleTextView = findViewById(R.id.title_tv);
        titleTextView.setText(movieDetail.getTitle());

        TextView plotTextView = findViewById(R.id.plot_tv);
        plotTextView.setText(movieDetail.getOverview());

        TextView releaseDateTextView = findViewById(R.id.realease_date_tv);
        releaseDateTextView.setText(movieDetail.getReleaseDate());

        TextView voteAverageTextView = findViewById(R.id.vote_tv);
        voteAverageTextView.setText(String.valueOf(movieDetail.getVoteAverage()));
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie not available", Toast.LENGTH_SHORT).show();
    }
}
