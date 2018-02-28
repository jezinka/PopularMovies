package com.projects.jezinka.popularmovies.Service;

import com.projects.jezinka.popularmovies.Model.MovieDetailsList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {
    @GET("{param}")
    Call<MovieDetailsList> loadMovies(
            @Path("param") String param,
            @Query("api_key") String apiKey);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
