package com.projects.jezinka.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projects.jezinka.popularmovies.R;
import com.projects.jezinka.popularmovies.model.MovieReview;

public class ReviewsAdapter extends ArrayAdapter<MovieReview> {
    private MovieReview[] movieReviews;

    public ReviewsAdapter(Context c, MovieReview[] movieReviews) {
        super(c, R.layout.review_list_item, movieReviews);
        this.movieReviews = movieReviews;
    }

    public int getCount() {
        return movieReviews.length;
    }

    public MovieReview getItem(int position) {
        return movieReviews[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        MovieReview dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.review_list_item, parent, false);
            viewHolder.author = convertView.findViewById(R.id.review_author_tv);
            viewHolder.content = convertView.findViewById(R.id.review_content_tv);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.author.setText(dataModel.getAuthor());
        viewHolder.content.setText(dataModel.getContent());
        return result;
    }

    private static class ViewHolder {
        TextView author;
        TextView content;
    }
}
