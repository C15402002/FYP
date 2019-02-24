package com.example.myapplication.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.ProductClickedListener;

public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderId, orderStatus, orderPhone;

    private ProductClickedListener productClickListener;
    public OrderHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.orderNum);
        orderStatus = itemView.findViewById(R.id.status);
        orderPhone = itemView.findViewById(R.id.phone);

        itemView.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        productClickListener.onClick(view, getAdapterPosition(), false);


    }

    public void setItemClickListener(ProductClickedListener productClickListener){
        this.productClickListener = productClickListener;
    }

}
