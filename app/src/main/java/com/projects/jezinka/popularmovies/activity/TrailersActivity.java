package com.projects.jezinka.popularmovies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.projects.jezinka.popularmovies.BuildConfig;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.adapter.TrailersAdapter;
import com.projects.jezinka.popularmovies.model.GenericList;
import com.projects.jezinka.popularmovies.model.MovieVideo;
import com.projects.jezinka.popularmovies.service.TheMovieDbService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersActivity extends AppCompatActivity {

    public static final String TAG = "TrailersActivity";

    @BindView(R.id.trailers_gv)
    GridView trailersGridView;

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
        final Call<GenericList<MovieVideo>> call = theMovieDbService.loadVideos(id, BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<GenericList<MovieVideo>>() {
            @Override
            public void onResponse(@NonNull Call<GenericList<MovieVideo>> call, @NonNull Response<GenericList<MovieVideo>> response) {

                GenericList<MovieVideo> body = response.body();
                MovieVideo[] results = body.getResults();
                Log.i("test", "Results length: " + String.valueOf(results.length));
                TrailersAdapter adapter = new TrailersAdapter(mContext, results);
                trailersGridView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<GenericList<MovieVideo>> call, @NonNull Throwable t) {
                Log.i(TAG, getResources().getString(R.string.no_results));
            }
        });
    }
}
