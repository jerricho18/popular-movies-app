package com.example.popularmoviesclone.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesclone.R;
import com.example.popularmoviesclone.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter {

    List<Review> reviewList = new ArrayList<>();

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
        return new ReviewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ReviewItemViewHolder) holder).bindData(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewItemViewHolder extends RecyclerView.ViewHolder {

        TextView reviewTextView, authorTextView;

        public ReviewItemViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewTextView = itemView.findViewById(R.id.tv_review_text);
            authorTextView = itemView.findViewById(R.id.tv_review_author);
        }

        public void bindData(Review review) {
            Log.d("JER", "BINDING REVIEW DATA");
            String author = "- Reviewed by: " + review.getAuthor();
            reviewTextView.setText(review.getContent());
            authorTextView.setText(author);
        }
    }
}
