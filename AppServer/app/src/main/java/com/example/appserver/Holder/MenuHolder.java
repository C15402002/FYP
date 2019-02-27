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

public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView fdName,fdDescript, fdPrice;
    public ImageView fdImage;

    private ItemClickedListener menuClickListener;
    public MenuHolder(@NonNull View itemView){
        super(itemView);
        fdName=(TextView)itemView.findViewById(R.id.foodname);
        fdImage=(ImageView)itemView.findViewById(R.id.foodimage);
        fdDescript = itemView.findViewById(R.id.foodDescript);
        fdPrice = itemView.findViewById(R.id.foodPrice);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickedListener menuClickListener){
        this.menuClickListener= menuClickListener;
    }

    @Override
    public void onClick(View view){
        menuClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Choose Action");
        contextMenu.add(0,0,getAdapterPosition(), Control.update);
        contextMenu.add(0,1,getAdapterPosition(), Control.delete);

    }
}
