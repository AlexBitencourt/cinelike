package com.example.android.cineliketrailer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cineliketrailer.R;
import com.example.android.cineliketrailer.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alexbitencourt on 28/07/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_REVIEWS = 0;

    private List<MovieReview> reviews;
    private Context context;


    @BindView(R.id.list_item_author)
     TextView reviewAuthor;

     @BindView(R.id.list_item_content)
      TextView reviewContent;


    public ReviewAdapter(List<MovieReview> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    public void setReviews(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reviews, parent, false);
        ReviewAdapter.ViewHolderReviews viewHolderReviews = new ReviewAdapter.ViewHolderReviews(itemView);
        return viewHolderReviews;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == 0) {
            ReviewAdapter.ViewHolderReviews viewHolderReviews = (ReviewAdapter.ViewHolderReviews) holder;
            viewHolderReviews.bindViews(position);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_REVIEWS;
        } else {
            return 0;
        }
    }

    public class ViewHolderReviews extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_author)
        TextView reviewAuthor;

        @BindView(R.id.list_item_content)
        TextView reviewContent;


        public ViewHolderReviews(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        public void bindViews(int position) {
            reviewAuthor.setText(reviews.get(position).getReviewAuthor());
            reviewContent.setText(reviews.get(position).getReviewContent());

        }

    }

}