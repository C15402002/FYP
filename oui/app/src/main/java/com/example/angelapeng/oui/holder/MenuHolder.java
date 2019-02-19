package com.example.angelapeng.oui.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angelapeng.oui.R;
import com.example.angelapeng.oui.view.ProductClickedListener;

public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView fdName;
    public ImageView fdImage;

    private ProductClickedListener menuClickListener;
    public MenuHolder(@NonNull View itemView){
        super(itemView);
        fdName=(TextView)itemView.findViewById(R.id.foodname);
        fdImage=(ImageView)itemView.findViewById(R.id.foodimage);
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
