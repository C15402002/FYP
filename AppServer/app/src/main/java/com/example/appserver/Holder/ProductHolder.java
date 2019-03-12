package com.example.appserver.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appserver.R;
import com.example.appserver.control.Control;
import com.example.appserver.view.ItemClickedListener;


public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView itemName;
    public ImageView itemImage;

    private ItemClickedListener itemClickedListener;

    public ProductHolder(@NonNull View itemView) {
        super(itemView);
        itemName = (TextView)itemView.findViewById(R.id.mname);
        itemImage = (ImageView)itemView.findViewById(R.id.mimage);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemClickListener(ItemClickedListener itemClickedListener){
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public void onClick(View view) {
        itemClickedListener.onClick(view, getAdapterPosition(), false);

    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Choose Action");
        contextMenu.add(0,0,getAdapterPosition(), Control.update);
        contextMenu.add(0,1,getAdapterPosition(), Control.delete);

    }
}