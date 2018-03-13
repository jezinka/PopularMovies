package com.projects.jezinka.popularmovies.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.jezinka.popularmovies.Model.MovieDetails;
import com.projects.jezinka.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_DETAILS = "MOVIE_DETAILS";
    public static final String MOVIE_ID = "ID";
    private static final String TAG = "Details Activity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        final MovieDetails movieDetail = intent.getExtras().getParcelable(MOVIE_DETAILS);

        if (movieDetail == null) {
            closeOnError();
        }

        Picasso.with(this)
                .load(movieDetail.getDetailPosterPath())
                .placeholder(android.R.drawable.star_off)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);

        titleTextView.setText(movieDetail.getTitle());
        plotTextView.setText(movieDetail.getOverview());
        releaseDateTextView.setText(movieDetail.getReleaseDate());
        voteAverageTextView.setText(String.valueOf(movieDetail.getVoteAverage()));

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent intent = new Intent(context, ReviewsActivity.class);
                intent.putExtra(MOVIE_ID, movieDetail.getId());
                context.startActivity(intent);
            }
        });

        trailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent intent = new Intent(context, TrailersActivity.class);
                intent.putExtra(MOVIE_ID, movieDetail.getId());
                context.startActivity(intent);
            }
        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie not available", Toast.LENGTH_SHORT).show();
    }
}
