package com.example.myapplication.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.view.ProductClickedListener;

public class   OrderHolder extends RecyclerView.ViewHolder {

    public TextView orderId, orderStatus, orderPrice, orderTable;
    public ImageButton deleteOrder;

    private ProductClickedListener productClickListener;
    public OrderHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.orderNum);
        orderStatus = itemView.findViewById(R.id.status);
        orderPrice = itemView.findViewById(R.id.price);
        orderTable = itemView.findViewById(R.id.table);
        deleteOrder = itemView.findViewById(R.id.removeOrder);


    }




}
