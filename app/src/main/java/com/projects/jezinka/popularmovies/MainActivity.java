package com.projects.jezinka.popularmovies;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = findViewById(R.id.gridview);

        new FetchMovieTask().execute();

//        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);
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
            e.printStackTrace();
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

    public class FetchMovieTask extends AsyncTask<String, Void, List<MovieDetails>> {

        @Override
        protected List<MovieDetails> doInBackground(String... params) {
            List<MovieDetails> moviesList = new ArrayList<>();

            URL movieRequestUrl = buildUrl();

            try {
                String movies = getResponseFromHttpUrl(movieRequestUrl);
                JSONObject moviesJSON = new JSONObject(movies);
                if (moviesJSON.has("results")) {
                    JSONArray moviesJSONArray = moviesJSON.getJSONArray("results");
                    for (int i = 0; i < moviesJSONArray.length(); i++) {
                        JSONObject movie = moviesJSONArray.getJSONObject(i);
                        MovieDetails movieDetails = new MovieDetails(movie);
                        moviesList.add(movieDetails);
                    }
                }
                return moviesList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieDetails> movieDetails) {
            Log.i(TAG, movieDetails.toString());
        }
    }
}
