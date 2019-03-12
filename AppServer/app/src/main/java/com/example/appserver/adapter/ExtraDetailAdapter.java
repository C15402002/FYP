package com.example.appserver.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appserver.R;
import com.example.appserver.model.Order;

import java.util.List;

class MyHolder extends RecyclerView.ViewHolder{

    public TextView name, quantity, price;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.menuDish);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);
    }
}
public class ExtraDetailAdapter extends RecyclerView.Adapter<MyHolder>{

    List<Order> myOrder;

    public ExtraDetailAdapter(List<Order> myOrder) {
        this.myOrder = myOrder;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.extra_detail_layout, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        Order order = myOrder.get(i);
        myHolder.name.setText(order.getProdName());
        myHolder.quantity.setText(order.getQuantity());
        myHolder.price.setText(order.getPrice());
    }

    @Override
    public int getItemCount() {
        return myOrder.size();
    }
}
