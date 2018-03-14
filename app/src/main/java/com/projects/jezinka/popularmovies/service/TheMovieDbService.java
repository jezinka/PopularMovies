package com.projects.jezinka.popularmovies.service;

import com.projects.jezinka.popularmovies.model.GenericList;
import com.projects.jezinka.popularmovies.model.MovieDetails;
import com.projects.jezinka.popularmovies.model.MovieReview;
import com.projects.jezinka.popularmovies.model.MovieVideo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {
    @GET("{param}")
    Call<GenericList<MovieDetails>> loadMovies(
            @Path("param") String param,
            @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<GenericList<MovieReview>> loadReviews(
            @Path("id") String id,
            @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<GenericList<MovieVideo>> loadVideos(
            @Path("id") String id,
            @Query("api_key") String apiKey);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
