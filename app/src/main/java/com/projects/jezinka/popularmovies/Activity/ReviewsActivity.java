package com.projects.jezinka.popularmovies.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.projects.jezinka.popularmovies.Adapter.ReviewsAdapter;
import com.projects.jezinka.popularmovies.BuildConfig;
import com.projects.jezinka.popularmovies.Model.MovieReview;
import com.projects.jezinka.popularmovies.Model.MovieReviewList;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.Service.TheMovieDbService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";

    @BindView(R.id.review_list)
    ListView reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this);
        String movieId = getIntent().getStringExtra(DetailsActivity.MOVIE_ID);
        sendQueryForReviews(movieId);
    }


    private void sendQueryForReviews(String id) {
        final Context mContext = this;
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<MovieReviewList> call = theMovieDbService.loadReviews(id, BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<MovieReviewList>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewList> call, @NonNull Response<MovieReviewList> response) {

                MovieReviewList body = response.body();
                MovieReview[] results = body.getResults();
                Log.i("test", "Results length: " + String.valueOf(results.length));
                ReviewsAdapter adapter = new ReviewsAdapter(mContext, results);
                reviewList.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewList> call, @NonNull Throwable t) {
                Log.i(TAG, getResources().getString(R.string.no_results));
            }
        });
    }
}
