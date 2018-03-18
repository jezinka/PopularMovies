package com.projects.jezinka.popularmovies.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.projects.jezinka.popularmovies.BuildConfig;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.adapter.MoviePostersAdapter;
import com.projects.jezinka.popularmovies.model.GenericList;
import com.projects.jezinka.popularmovies.model.MovieDetails;
import com.projects.jezinka.popularmovies.service.TheMovieDbService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.CONTENT_URI;
import static com.projects.jezinka.popularmovies.data.MovieDetailsContract.MovieDetailsEntry.TITLE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITES = "favorites";

    private static final int LOADER_ID = 0x01;
    private MoviePostersAdapter adapter;

    @BindView(R.id.gridview)
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new MoviePostersAdapter(this);
        gridview.setAdapter(adapter);

        String sortingParam = loadSortingPreference();
        getMovieList(sortingParam);
    }

    @NonNull
    private String loadSortingPreference() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.sort_criterion_key), POPULAR);
    }

    private void getMovieList(String param) {
        if (param.equals(FAVORITES)) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            sendQueryForMovies(param);
        }

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
                    adapter.updateResults(body.getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericList<MovieDetails>> call, @NonNull Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                Toast.makeText(mContext, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, TITLE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        MovieDetails[] data;
        if (cursor != null && cursor.moveToFirst()) {
            data = new MovieDetails[cursor.getCount()];
            do {
                data[cursor.getPosition()] = new MovieDetails(cursor);

            } while (cursor.moveToNext());
            adapter.updateResults(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String sortingParam;

        switch (item.getItemId()) {
            case R.id.popular:
                sortingParam = POPULAR;
                break;
            case R.id.rated:
                sortingParam = TOP_RATED;
                break;
            case R.id.favorites:
                sortingParam = FAVORITES;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        saveSortingPreference(sortingParam);
        getMovieList(sortingParam);

        return true;
    }

    private void saveSortingPreference(String sortingParam) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sort_criterion_key), sortingParam);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
