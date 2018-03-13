package com.projects.jezinka.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projects.jezinka.popularmovies.Model.MovieVideo;
import com.projects.jezinka.popularmovies.R;

public class TrailersAdapter extends ArrayAdapter<MovieVideo> {
    private MovieVideo[] movieVideos;

    public TrailersAdapter(Context c, MovieVideo[] movieVideos) {
        super(c, R.layout.trailer_list_item, movieVideos);
        this.movieVideos = movieVideos;
    }

    public int getCount() {
        return movieVideos.length;
    }

    public MovieVideo getItem(int position) {
        return movieVideos[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final MovieVideo dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trailer_list_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.video_title_tv);
            viewHolder.trailersButton = convertView.findViewById(R.id.video_btn);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText(dataModel.getName());
        viewHolder.trailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.youtube.com/watch?v=" + dataModel.getKey();
                Uri webpage = Uri.parse(uri);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                getContext().startActivity(webIntent);
            }
        });
        return result;
    }

    private static class ViewHolder {
        TextView name;
        ImageButton trailersButton;
    }
}
