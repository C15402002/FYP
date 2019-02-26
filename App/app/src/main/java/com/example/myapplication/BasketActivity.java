package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.BasketAdapter;
import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.model.MakeOrder;
import com.example.myapplication.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BasketActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView totalPrice;
    Button placeOrder;

    List<Order> orderItems = new ArrayList<>();
    BasketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("OrderPlaced");

        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        totalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrder);

        orderItems = new Database(this).getOrderBasket();
        adapter = new BasketAdapter(orderItems, this);
        recyclerView.setAdapter(adapter);

        Locale locale = new Locale("en","IE");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        float total = 0;
        for(Order order:orderItems){
            total += (Float.parseFloat(order.getPrice()))*(Float.parseFloat(order.getQuantity()));
        }

        totalPrice.setText(numberFormat.format(total));

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeOrder makeOrder = new MakeOrder(Control.currentUser.getName(),
                        Control.currentUser.getEmail(), Control.currentUser.getPhone(),totalPrice.getText().toString(), orderItems);
                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(makeOrder);
                new Database(getBaseContext()).deleteFromBasket();
                Toast.makeText(BasketActivity.this, "Order sent to kitchen", Toast.LENGTH_SHORT).show();
                finish();
            }

        });



    }
}
