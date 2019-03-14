package com.example.myapplication.adapter;


import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.BasketActivity;
import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.model.Order;
import com.example.myapplication.view.ProductClickedListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class BasketHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView basketName, basketPrice;
    //public ImageView basketCount;
    public ElegantNumberButton counter;


    private ProductClickedListener productClickedListener;

    public void setBasketName(TextView basketName) {
        this.basketName = basketName;
    }

    public BasketHolder(@NonNull View itemView) {
        super(itemView);
        basketName = itemView.findViewById(R.id.itemName);
        basketPrice = itemView.findViewById(R.id.itemPrice);
        //basketCount = itemView.findViewById(R.id.basketCount);
        counter = itemView.findViewById(R.id.counter);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Choose Action");
        contextMenu.add(0,0,getAdapterPosition(), Control.delete);

    }
}

public class BasketAdapter extends RecyclerView.Adapter<BasketHolder> {

    private List<Order> listAddedItems = new ArrayList<>();
    //private Context con;
    private BasketActivity basketActivity;

    public BasketAdapter(List<Order> listAddedItems, BasketActivity basketActivity) {
        this.listAddedItems = listAddedItems;
        this.basketActivity = basketActivity;
    }

    @NonNull
    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(basketActivity);
        View view = inflater.inflate(R.layout.basket_layout, viewGroup,false);
        return new BasketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder basketHolder, final int i) {
//        TextDrawable textDrawable = TextDrawable.builder().buildRound("" + listAddedItems.get(i).getQuantity(), Color.GREEN);
//        basketHolder.basketCount.setImageDrawable(textDrawable);

        basketHolder.counter.setNumber(listAddedItems.get(i).getQuantity());
        basketHolder.counter.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listAddedItems.get(i);
                order.setQuantity(String.valueOf(newValue));
                new Database(basketActivity).editBasket(order);

               // basketActivity.totalPrice

                float total = 0;
                List<Order> listOfOrderPlaced = new Database(basketActivity).getOrderBasket(Control.currentUser.getPhone());
                for(Order item:listOfOrderPlaced){
                    total += (Float.parseFloat(order.getPrice()))*(Float.parseFloat(item.getQuantity()));
                }
                Locale locale = new Locale("en","IE");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                basketActivity.totalPrice.setText(numberFormat.format(total));
            }
        });

        Locale locale = new Locale("en","IE");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        float price = (Float.parseFloat(listAddedItems.get(i).getPrice()))*(Float.parseFloat(listAddedItems.get(i).getQuantity()));
        basketHolder.basketPrice.setText(numberFormat.format(price));
        basketHolder.basketName.setText(listAddedItems.get(i).getProductName());


    }

    @Override
    public int getItemCount() {
       return listAddedItems.size();
    }
}
