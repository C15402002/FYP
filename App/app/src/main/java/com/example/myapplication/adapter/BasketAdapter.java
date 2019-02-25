package com.example.myapplication.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.myapplication.R;
import com.example.myapplication.model.Order;
import com.example.myapplication.view.ProductClickedListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class BasketHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView basketName, basketPrice;
    public ImageView basketCount;

    private ProductClickedListener productClickedListener;

    public void setBasketName(TextView basketName) {
        this.basketName = basketName;
    }

    public BasketHolder(@NonNull View itemView) {
        super(itemView);
        basketName = itemView.findViewById(R.id.itemName);
        basketPrice = itemView.findViewById(R.id.itemPrice);
        basketCount = itemView.findViewById(R.id.basketCount);
    }

    @Override
    public void onClick(View view) {

    }
}

public class BasketAdapter extends RecyclerView.Adapter<BasketHolder> {

    private List<Order> listAddedItems = new ArrayList<>();
    private Context con;

    public BasketAdapter(List<Order> listAddedItems, Context con) {
        this.listAddedItems = listAddedItems;
        this.con = con;
    }

    @NonNull
    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(con);
        View view = inflater.inflate(R.layout.basket_layout, viewGroup,false);
        return new BasketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder basketHolder, int i) {
        TextDrawable textDrawable = TextDrawable.builder().buildRound("" + listAddedItems.get(i).getQuantity(), Color.RED);
        basketHolder.basketCount.setImageDrawable(textDrawable);

        Locale locale = new Locale("en","IE");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        float price = (Float.parseFloat(listAddedItems.get(i).getPrice()))*(Float.parseFloat(listAddedItems.get(i).getQuantity()));
        basketHolder.basketPrice.setText(numberFormat.format(price));
        basketHolder.basketName.setText(listAddedItems.get(i).getProdName());


    }

    @Override
    public int getItemCount() {
       return listAddedItems.size();
    }
}
