package com.example.appserver.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appserver.R;


public class OrderHolder extends RecyclerView.ViewHolder{

    public TextView orderId, orderStatus, orderPrice, orderTable, orderDate;
    public Button statusBtn, detailBtn, deleteBtn;

    public OrderHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.orderNum);
        orderStatus = itemView.findViewById(R.id.status);
        orderPrice = itemView.findViewById(R.id.price);
        orderTable = itemView.findViewById(R.id.table);
        orderDate = itemView.findViewById(R.id.OrderDate);
        statusBtn = itemView.findViewById(R.id.statusBtn);
        detailBtn = itemView.findViewById(R.id.detailbtn);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);


    }

}
