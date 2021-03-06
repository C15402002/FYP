package com.example.myapplication.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.ProductClickedListener;


public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView fdName,fdDescript, fdPrice;
    public ImageView fdImage;

    private ProductClickedListener menuClickListener;
    public MenuHolder(@NonNull View itemView){
        super(itemView);
        fdName=(TextView)itemView.findViewById(R.id.foodname);
        fdImage=(ImageView)itemView.findViewById(R.id.foodimage);
        fdDescript = itemView.findViewById(R.id.foodDescript);
        fdPrice = itemView.findViewById(R.id.foodprice);
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
