package com.example.appserver.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appserver.R;

import androidx.annotation.NonNull;

public class ReviewHolder extends RecyclerView.ViewHolder {
    public TextView identifyUser, review;
    public RatingBar stars;

    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        identifyUser = itemView.findViewById(R.id.userID);
        review = itemView.findViewById(R.id.review);
        stars = itemView.findViewById(R.id.ratingStar);
    }
}
