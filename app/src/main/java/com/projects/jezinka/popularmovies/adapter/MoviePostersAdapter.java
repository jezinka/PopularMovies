package com.projects.jezinka.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.projects.jezinka.popularmovies.activity.DetailsActivity;
import com.projects.jezinka.popularmovies.model.MovieDetails;
import com.squareup.picasso.Picasso;

public class MoviePostersAdapter extends BaseAdapter {
    private Context mContext;
    private MovieDetails[] mMovieDetails;

    public MoviePostersAdapter(Context c) {
        mContext = c;
        mMovieDetails = new MovieDetails[0];
    }

    public int getCount() {
        return mMovieDetails.length;
    }

    public MovieDetails getItem(int position) {
        return this.mMovieDetails[position];
    }

    public long getItemId(int position) {
        return Long.valueOf(this.getItem(position).getId());
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 280));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mMovieDetails[position].getListPosterPath())
                .placeholder(android.R.drawable.star_off)
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE_DETAILS, mMovieDetails[position]);
                mContext.startActivity(intent);
            }
        });
        return imageView;
    }

    public void updateResults(MovieDetails[] results) {
        this.mMovieDetails = results;
        this.notifyDataSetChanged();
    }
}
