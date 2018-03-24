package com.projects.jezinka.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.activity.DetailsActivity;
import com.projects.jezinka.popularmovies.model.MovieDetails;
import com.squareup.picasso.Picasso;

public class MoviePostersAdapter extends RecyclerView.Adapter<MoviePostersAdapter.ViewHolder> {
    private Context mContext;
    private MovieDetails[] mMovieDetails;

    public MoviePostersAdapter(Context c) {
        mContext = c;
        mMovieDetails = new MovieDetails[0];
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getItemCount() {
        return mMovieDetails.length;
    }

    private MovieDetails getItem(int position) {
        return this.mMovieDetails[position];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View posterView = layoutInflater.inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(posterView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieDetails movieDetails = mMovieDetails[position];

        ImageView posterImageView = holder.posterImageView;

        Picasso.with(mContext)
                .load(movieDetails.getListPosterPath())
                .placeholder(android.R.drawable.star_off)
                .error(android.R.drawable.stat_notify_error)
                .resize(150, 200)
                .onlyScaleDown()
                .centerInside()
                .into(posterImageView);

        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE_DETAILS, movieDetails);
                mContext.startActivity(intent);
            }
        });
    }

    public long getItemId(int position) {
        return Long.valueOf(this.getItem(position).getId());
    }

    public void updateResults(MovieDetails[] results) {
        this.mMovieDetails = results;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;

        ViewHolder(View itemView) {
            super(itemView);

            posterImageView = itemView.findViewById(R.id.poster_iv);
        }
    }
}
