package com.example.android.cineliketrailer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cineliketrailer.R;
import com.example.android.cineliketrailer.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alexbitencourt on 28/07/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final String YOUTUBE_BASE = "https://www.youtube.com/watch?v=";

    private final int VIEW_TYPE_TRAILERS = 0;
    private List<MovieTrailer> trailers;
    private Context context;


    public TrailerAdapter(List<MovieTrailer> trailers, Context context) {
        this.trailers = trailers;
        this.context = context;
    }

    public void setTrailers(ArrayList<MovieTrailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TRAILERS;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailers, parent, false);
        TrailerAdapter.ViewHolderTrailers viewHolderTrailers = new TrailerAdapter.ViewHolderTrailers(itemView);
        return viewHolderTrailers;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if(holder.getItemViewType() == 0) {
           TrailerAdapter.ViewHolderTrailers viewHolderTrailers = (TrailerAdapter.ViewHolderTrailers) holder;
           viewHolderTrailers.bindViews(context, position);
       }
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public MovieTrailer getItem(int position) {
        return trailers.get(position);
    }

    public class ViewHolderTrailers extends RecyclerView.ViewHolder {

        @BindView(R.id.list_trailer_name)
        TextView trailerTitle;

        @BindView(R.id.list_trailer_image)
        ImageView trailerImage;


        public ViewHolderTrailers(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bindViews(final Context context, int position) {
            trailerTitle.setText(trailers.get(position).getTrailerName());
            Picasso.with(context)
                    .load(trailers.get(position).getYoutube_thumb()).into(trailerImage);

            MovieTrailer currentVideo = getItem(position);
            final String key = currentVideo.getTrailerKey();
            trailerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = YOUTUBE_BASE + key;
                    Log.i("Youtube URL :", url);
                    Intent whatchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    whatchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    context.startActivity(whatchIntent);
                }
            });

        }
    }

}

