package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.myapplication.control.Control;
import com.example.myapplication.holder.OrderHolder;
import com.example.myapplication.model.MakeOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderPlacedActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    FirebaseDatabase db;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<MakeOrder> options;
    FirebaseRecyclerAdapter<MakeOrder, OrderHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("OrderPlaced");

        recyclerView = findViewById(R.id.orderHistory);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        load(Control.currentUser.getPhone());


    }
    private void load(String phone){

        options = new FirebaseRecyclerOptions.Builder<MakeOrder>().setQuery(databaseReference.orderByChild("phone").equalTo(phone), MakeOrder.class).build();
        adapter = new FirebaseRecyclerAdapter<MakeOrder, OrderHolder>(options){
            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull MakeOrder model) {
                holder.orderId.setText(adapter.getRef(position).getKey());
                holder.orderPhone.setText(model.getPhone());
                holder.orderStatus.setText(model.getStatus());

            }



        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private String convertStatue(String status){
        if(status.equals("0")){
            return "Order Made";
        }else if(status.equals("1"))
        {
            return "Sent to kitchen";
        }else
        {
            return "Bought to table";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }



}
