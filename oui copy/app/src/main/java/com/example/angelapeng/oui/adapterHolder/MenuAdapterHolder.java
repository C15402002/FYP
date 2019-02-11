package com.example.angelapeng.oui.adapterHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angelapeng.oui.R;
import com.example.angelapeng.oui.view.productClickedListener;

public class MenuAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pName;
    public ImageView pImage;

    private com.example.angelapeng.oui.view.productClickedListener productClickedListener;


    public MenuAdapterHolder(@NonNull View itemView) {
        super(itemView);

        pName = itemView.findViewById(R.id.pname);
        pImage = itemView.findViewById(R.id.pimage);

        itemView.setOnClickListener(this);
    }
    public void setProductClickedListener(productClickedListener productClickedListener){

        this.productClickedListener = productClickedListener;
    }

    @Override
    public void onClick(View view) {
        productClickedListener.onClick(view , getAdapterPosition(), false);

    }
}
