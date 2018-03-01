package com.projects.jezinka.popularmovies.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.projects.jezinka.popularmovies.Adapter.MoviePostersAdapter;
import com.projects.jezinka.popularmovies.BuildConfig;
import com.projects.jezinka.popularmovies.Model.MovieDetailsList;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.Service.TheMovieDbService;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";

    @BindView(R.id.gridview)
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendQueryForMovies(this, POPULAR);
    }

    private void sendQueryForMovies(final Context mContext, String param) {
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<MovieDetailsList> call = theMovieDbService.loadMovies(param, BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<MovieDetailsList>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailsList> call, @NonNull Response<MovieDetailsList> response) {

                MovieDetailsList body = response.body();
                MoviePostersAdapter adapter = new MoviePostersAdapter(mContext, body.getResults());
                gridview.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailsList> call, @NonNull Throwable t) {
                Log.i(TAG, getResources().getString(R.string.no_results));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.popular:
                sendQueryForMovies(this, POPULAR);
                return true;
            case R.id.rated:
                sendQueryForMovies(this, TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
