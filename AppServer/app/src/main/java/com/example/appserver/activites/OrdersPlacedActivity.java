package com.example.appserver.activites;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appserver.holder.OrderHolder;
import com.example.appserver.R;
import com.example.appserver.config.RemoteAPIService;
import com.example.appserver.control.Control;
import com.example.appserver.model.MakeOrder;
import com.example.appserver.model.Notification;
import com.example.appserver.model.Response;
import com.example.appserver.model.Sender;
import com.example.appserver.model.Token;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import retrofit2.Call;
import retrofit2.Callback;

public class OrdersPlacedActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase db;
    DatabaseReference orderPlaced;

    RemoteAPIService remoteAPIService;


    FirebaseRecyclerOptions<MakeOrder> options;
    FirebaseRecyclerAdapter<MakeOrder, OrderHolder> adapter;

    MaterialSpinner materialSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_placed);

        recyclerView = findViewById(R.id.orderHistory);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
       /// ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        db = FirebaseDatabase.getInstance();
        orderPlaced = db.getReference("Restaurant").child(Control.currentUser.getRestId()).child("OrderPlaced");

        remoteAPIService = Control.getCloudMessage();


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton) findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        options = new FirebaseRecyclerOptions.Builder<MakeOrder>().setQuery(orderPlaced, MakeOrder.class).build();
        adapter = new FirebaseRecyclerAdapter<MakeOrder, OrderHolder>(options){

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history,viewGroup,false);
                return new OrderHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final OrderHolder holder, final int position, @NonNull final MakeOrder model) {
                holder.orderId.setText(adapter.getRef(position).getKey());
                holder.orderStatus.setText(Control.convertStatus(model.getStatus()));
                holder.orderPrice.setText(model.getTotal());
                holder.orderTable.setText(model.getTable());
                holder.orderDate.setText(Control.orderDate(Long.parseLong(adapter.getRef(position).getKey())));
                holder.orderPayMeth.setText(model.getPaymentMethods());
                holder.orderPayProcess.setText(model.getPaymentProcess());

                holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeleteDialog(adapter.getRef(position).getKey());
                    }
                });
                holder.statusBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {

                            showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));

                    }
                });
                holder.detailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrdersPlacedActivity.this, OrderDetailActivity.class);
                        intent.putExtra("OrderId", adapter.getRef(position).getKey());
                        Control.currentOrder = model;
                        startActivity(intent);
                    }
                });


            }

        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }


    private void showDeleteDialog(String key) {
        orderPlaced.child(key).removeValue();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Item removed!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final MakeOrder item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersPlacedActivity.this);
        alertDialog.setTitle("Update Status");
        alertDialog.setMessage("Please choose: ");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.update_status_layout, null);

        alertDialog.setView(view);

        final String key_STAT = key;

        materialSpinner = view.findViewById(R.id.spinner);
        materialSpinner.setItems("Kitchen", "Cooking", "Served");

        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(materialSpinner.getSelectedIndex()));
                orderPlaced.child(key_STAT).setValue(item);

                if(item.getStatus().equals("1")){
                    item.setPaymentProcess("Paid");
                }
                adapter.notifyDataSetChanged();
                sendStatus(key_STAT,item);


            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendStatus(final String key, final MakeOrder item) {
        DatabaseReference databaseReference = db.getReference("Tokens");
        databaseReference.orderByKey().equalTo(item.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Token token = postSnapShot.getValue(Token.class);
                    Notification notification = new Notification("OUI", "Your order " + key + " was updated");
                    Sender sender = new Sender(token.getToken(), notification);

                    remoteAPIService.sendNotice(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if (response.body() != null) {
                                if(response.body().success == 1){
                                    Toast.makeText(OrdersPlacedActivity.this, "Order updated!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else{
                                    Toast.makeText(OrdersPlacedActivity.this, "Error something happened!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Toast.makeText(OrdersPlacedActivity.this, "Error something happened!", Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
