package com.example.appserver.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appserver.R;
import com.example.appserver.view.ItemClickedListener;

public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView fdName;
    public ImageView fdImage;

    private ItemClickedListener menuClickListener;
    public MenuHolder(@NonNull View itemView){
        super(itemView);
        fdName=(TextView)itemView.findViewById(R.id.mname);
        fdImage=(ImageView)itemView.findViewById(R.id.mimage);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickedListener menuClickListener){
        this.menuClickListener= menuClickListener;
    }

    @Override
    public void onClick(View view){
        menuClickListener.onClick(view,getAdapterPosition(),false);

    }
}
