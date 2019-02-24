package com.example.myapplication.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.ProductClickedListener;


public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemName;
    public ImageView itemImage;

    private ProductClickedListener productClickListener;
    public ProductHolder(@NonNull View itemView) {
        super(itemView);
        itemName = (TextView)itemView.findViewById(R.id.mname);
        itemImage = (ImageView)itemView.findViewById(R.id.mimage);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ProductClickedListener productClickListener){
        this.productClickListener = productClickListener;
    }

    @Override
    public void onClick(View view) {
        productClickListener.onClick(view, getAdapterPosition(), false);

    }
}
