package com.example.popularmoviesclone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesclone.R;
import com.example.popularmoviesclone.models.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter {

    private List<Trailer> trailerList = new ArrayList<>();
    private TrailerItemClickListener onClickListener;

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    public Trailer getTrailerByIndex(int index) {
        return trailerList.get(index);
    }

    public TrailerListAdapter(TrailerItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface TrailerItemClickListener {
        void onTrailerItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_layout, parent, false);
        return new TrailerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TrailerItemViewHolder) holder).bindData(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailerNameTextView;

        public TrailerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerNameTextView = itemView.findViewById(R.id.tv_trailer_name);
            trailerNameTextView.setOnClickListener(this);
        }

        public void bindData(Trailer trailer) {
            trailerNameTextView.setText(trailer.getName());
        }

        @Override
        public void onClick(View view) {
            onClickListener.onTrailerItemClick(getAdapterPosition());
        }
    }
}
