package com.example.myapplication.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.ProductClickedListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantHolder extends RecyclerView.ViewHolder{

    public TextView restName;
    public ImageView restImage, restShare;

    public RestaurantHolder(@NonNull View itemView){
        super(itemView);
        restName=(TextView)itemView.findViewById(R.id.restname);
        restImage=(ImageView)itemView.findViewById(R.id.restimage);
        restShare = (ImageView) itemView.findViewById(R.id.share);


    }


}

