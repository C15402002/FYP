package com.example.myapplication.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.ProductClickedListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView restName;
    public ImageView restImage;

    private ProductClickedListener menuClickListener;
    public RestaurantHolder(@NonNull View itemView){
        super(itemView);
        restName=(TextView)itemView.findViewById(R.id.mname);
        restImage=(ImageView)itemView.findViewById(R.id.mimage);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ProductClickedListener menuClickListener){
        this.menuClickListener= menuClickListener;
    }

    @Override
    public void onClick(View view){
        menuClickListener.onClick(view,getAdapterPosition(),false);

    }

}

