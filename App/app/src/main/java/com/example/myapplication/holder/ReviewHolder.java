package com.example.myapplication.holder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewHolder extends RecyclerView.ViewHolder {
    public TextView identifyUser, review;
    public RatingBar stars;

    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        identifyUser = itemView.findViewById(R.id.userID);
        review = itemView.findViewById(R.id.seeReviews);
        stars = itemView.findViewById(R.id.ratingStar);
    }
}
