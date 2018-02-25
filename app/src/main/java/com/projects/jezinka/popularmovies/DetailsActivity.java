package com.projects.jezinka.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";
    public static final String PLOT = "PLOT";
    public static final String RELEASE_DATE = "RELEASE_DATE";
    public static final String VOTE_AVERAGE = "VOTE_AVERAGE";

    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        TextView titleTextView = findViewById(R.id.title_tv);
        titleTextView.setText(intent.getStringExtra(TITLE));

        TextView plotTextView = findViewById(R.id.plot_tv);
        plotTextView.setText(intent.getStringExtra(PLOT));

        TextView releaseDateTextView = findViewById(R.id.realease_date_tv);
        releaseDateTextView.setText(intent.getStringExtra(RELEASE_DATE));

        TextView voteAverageTextView = findViewById(R.id.vote_tv);
        voteAverageTextView.setText(String.valueOf(intent.getDoubleExtra(VOTE_AVERAGE, 0.0d)));
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie not available", Toast.LENGTH_SHORT).show();
    }
}
