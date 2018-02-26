package com.projects.jezinka.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.projects.jezinka.popularmovies.Activity.DetailsActivity;
import com.projects.jezinka.popularmovies.Model.MovieDetails;
import com.squareup.picasso.Picasso;

public class MoviePostersAdapter extends BaseAdapter {
    private Context mContext;
    private MovieDetails[] mMovieDetails;

    public MoviePostersAdapter(Context c, MovieDetails[] movieDetails) {
        mContext = c;
        mMovieDetails = movieDetails;
    }

    public int getCount() {
        return mMovieDetails.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
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

        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w185" + mMovieDetails[position].getPoster()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);

                intent.putExtra(DetailsActivity.ID, mMovieDetails[position].getId());

                mContext.startActivity(intent);
            }
        });
        return imageView;
    }
}