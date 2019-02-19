package com.example.angelapeng.oui.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angelapeng.oui.R;
import com.example.angelapeng.oui.view.ProductClickedListener;


public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemName;
    public ImageView itemImage;

    private ProductClickedListener productClickListener;
    public MenuHolder(@NonNull View itemView) {
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
