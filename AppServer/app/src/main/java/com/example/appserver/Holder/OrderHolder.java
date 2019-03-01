package com.example.appserver.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appserver.R;
import com.example.appserver.control.Control;
import com.example.appserver.view.ItemClickedListener;


public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView orderId, orderStatus, orderPrice, orderTable;

    private ItemClickedListener itemClickedListener;
    public OrderHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.orderNum);
        orderStatus = itemView.findViewById(R.id.status);
        orderPrice = itemView.findViewById(R.id.price);
        orderTable = itemView.findViewById(R.id.table);


        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        itemClickedListener.onClick(view, getAdapterPosition(), false);


    }

    public void setItemClickListener(ItemClickedListener itemClickedListener){
        this.itemClickedListener = itemClickedListener;
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Choose Action");
        contextMenu.add(0,0,getAdapterPosition(), Control.update);
        contextMenu.add(0,1,getAdapterPosition(), Control.delete);

    }
}
