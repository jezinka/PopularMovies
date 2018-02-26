package com.projects.jezinka.popularmovies.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.projects.jezinka.popularmovies.Adapter.MoviePostersAdapter;
import com.projects.jezinka.popularmovies.Model.MovieDetails;
import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String RESULTS = "results";

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = findViewById(R.id.gridview);

        new FetchMovieTask(this).execute("popular");
    }


    public class FetchMovieTask extends AsyncTask<String, Void, MovieDetails[]> {

        Context taskContext;

        public FetchMovieTask(Context taskContext) {
            this.taskContext = taskContext;
        }

        @Override
        protected MovieDetails[] doInBackground(String... params) {
            MovieDetails[] moviesList = new MovieDetails[20];

            URL movieRequestUrl = NetworkUtils.buildUrl(params[0]);

            try {
                String movies = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                JSONObject moviesJSON = new JSONObject(movies);
                if (moviesJSON.has(RESULTS)) {
                    JSONArray moviesJSONArray = moviesJSON.getJSONArray(RESULTS);
                    for (int i = 0; i < moviesJSONArray.length(); i++) {
                        JSONObject movie = moviesJSONArray.getJSONObject(i);
                        MovieDetails movieDetails = new MovieDetails(movie);
                        moviesList[i] = movieDetails;
                    }
                }
                return moviesList;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetails[] movieDetails) {
            if (movieDetails != null && movieDetails.length > 0) {
                MoviePostersAdapter adapter = new MoviePostersAdapter(this.taskContext, movieDetails);
                gridview.setAdapter(adapter);
            } else {
                Log.i(TAG, "The query returns no results");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.popular:
                new FetchMovieTask(this).execute("popular");
                return true;
            case R.id.rated:
                new FetchMovieTask(this).execute("top_rated");
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
