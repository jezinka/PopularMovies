package com.projects.jezinka.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String RESULTS = "results";

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = findViewById(R.id.gridview);

        new FetchMovieTask(this).execute();
    }

    public static URL buildUrl() {
        Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/popular")
                .buildUpon()
                .appendQueryParameter("api_key", "xxxXxxxxXxxx")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public class FetchMovieTask extends AsyncTask<String, Void, MovieDetails[]> {

        Context taskContext;

        public FetchMovieTask(Context taskContext) {
            this.taskContext = taskContext;
        }

        @Override
        protected MovieDetails[] doInBackground(String... params) {
            MovieDetails[] moviesList = new MovieDetails[20];

            URL movieRequestUrl = buildUrl();

            try {
                String movies = getResponseFromHttpUrl(movieRequestUrl);
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
}
