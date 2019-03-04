package com.example.myapplication.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.ProductClickedListener;

public class   OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderId, orderStatus, orderPrice, orderTable;

    private ProductClickedListener productClickListener;
    public OrderHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.orderNum);
        orderStatus = itemView.findViewById(R.id.status);
        orderPrice = itemView.findViewById(R.id.price);
        orderTable = itemView.findViewById(R.id.table);

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
