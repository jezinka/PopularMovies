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
import com.projects.jezinka.popularmovies.Model.GenericList;
import com.projects.jezinka.popularmovies.Model.MovieDetails;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.Service.TheMovieDbService;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        ButterKnife.bind(this);

        sendQueryForMovies(POPULAR);
    }

    private void sendQueryForMovies(String param) {
        final Context mContext = this;
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<GenericList<MovieDetails>> call = theMovieDbService.loadMovies(param, BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<GenericList<MovieDetails>>() {
            @Override
            public void onResponse(@NonNull Call<GenericList<MovieDetails>> call, @NonNull Response<GenericList<MovieDetails>> response) {

                GenericList<MovieDetails> body = response.body();

                if (body != null) {
                    MoviePostersAdapter adapter = new MoviePostersAdapter(mContext, body.getResults());
                    gridview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericList<MovieDetails>> call, @NonNull Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.popular:
                sendQueryForMovies(POPULAR);
                return true;
            case R.id.rated:
                sendQueryForMovies(TOP_RATED);
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
