package com.projects.jezinka.popularmovies.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.projects.jezinka.popularmovies.Adapter.TrailersAdapter;
import com.projects.jezinka.popularmovies.BuildConfig;
import com.projects.jezinka.popularmovies.Model.MovieVideo;
import com.projects.jezinka.popularmovies.Model.MovieVideoList;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.Service.TheMovieDbService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersActivity extends AppCompatActivity {

    public static final String TAG = "TrailersActivity";

    @BindView(R.id.trailers_lv)
    ListView trailersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        ButterKnife.bind(this);

        String movieId = getIntent().getStringExtra(DetailsActivity.MOVIE_ID);
        sendQueryForReviews(movieId);
    }

    private void sendQueryForReviews(String id) {
        final Context mContext = this;
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<MovieVideoList> call = theMovieDbService.loadVideos(id, BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<MovieVideoList>() {
            @Override
            public void onResponse(@NonNull Call<MovieVideoList> call, @NonNull Response<MovieVideoList> response) {

                MovieVideoList body = response.body();
                MovieVideo[] results = body.getResults();
                Log.i("test", "Results length: " + String.valueOf(results.length));
                TrailersAdapter adapter = new TrailersAdapter(mContext, results);
                trailersListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<MovieVideoList> call, @NonNull Throwable t) {
                Log.i(TAG, getResources().getString(R.string.no_results));
            }
        });
    }
}