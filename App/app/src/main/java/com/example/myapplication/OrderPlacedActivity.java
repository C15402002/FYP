package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(OrderPlacedActivity.this, HomeActivity.class);
               startActivity(intent);
               finish();
            }
        });


        if(getIntent().getExtras() == null) {
            load(Control.currentUser.getPhone());

        }else{
            load(getIntent().getStringExtra("userPhone"));
        }

    }
    private void load(String phone){

        options = new FirebaseRecyclerOptions.Builder<MakeOrder>().setQuery(databaseReference.orderByChild("phone").equalTo(phone), MakeOrder.class).build();
        adapter = new FirebaseRecyclerAdapter<MakeOrder, OrderHolder>(options){
            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history,viewGroup,false);
                return new OrderHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull MakeOrder model) {
                holder.orderId.setText(adapter.getRef(position).getKey());
                holder.orderPrice.setText(model.getTotal());
                holder.orderStatus.setText(Control.convertStatus(model.getStatus()));
                holder.orderTable.setText(model.getTable());

            }



        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
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
