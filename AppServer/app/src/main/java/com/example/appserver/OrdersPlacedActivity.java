package com.example.appserver;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.appserver.Holder.OrderHolder;
import com.example.appserver.config.RemoteAPIService;
import com.example.appserver.control.Control;
import com.example.appserver.model.MakeOrder;
import com.example.appserver.model.Notification;
import com.example.appserver.model.Response;
import com.example.appserver.model.Sender;
import com.example.appserver.model.Token;
import com.example.appserver.view.ItemClickedListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;

public class OrdersPlacedActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase db;
    DatabaseReference orderPlaced;

    RemoteAPIService remoteAPIService;

    FirebaseRecyclerOptions<MakeOrder> options;
    FirebaseRecyclerAdapter<MakeOrder, OrderHolder> adapter;

     RadioGroup statusGroup;
     RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_placed);

        recyclerView = findViewById(R.id.orderHistory);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db = FirebaseDatabase.getInstance();
        orderPlaced = db.getReference("OrderPlaced");

        remoteAPIService = Control.getCloudMessage();


        options = new FirebaseRecyclerOptions.Builder<MakeOrder>().setQuery(orderPlaced, MakeOrder.class).build();
        adapter = new FirebaseRecyclerAdapter<MakeOrder, OrderHolder>(options){

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull MakeOrder model) {
                final MakeOrder clicked = model;
                holder.orderId.setText(adapter.getRef(position).getKey());
                holder.orderStatus.setText(Control.convertStatus(model.getStatus()));
                holder.orderPrice.setText(model.getTotal());
                holder.orderTable.setText(model.getTable());
                holder.setItemClickListener(new ItemClickedListener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClicked) {
                        if(isLongClicked){
                            Intent intent = new Intent(OrdersPlacedActivity.this, OrderDetailActivity.class);
                            intent.putExtra("OrderId", adapter.getRef(pos).getKey());
                            startActivity(intent);
                        }

                    }
                });


            }
            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history,viewGroup,false);
                return new OrderHolder(v);
            }


        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Control.update)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if(item.getTitle().equals(Control.delete)){
            showDeleteDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(String key, MakeOrder item) {
        orderPlaced.child(key).removeValue();
        Toast.makeText(this, "Item removed!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final MakeOrder item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersPlacedActivity.this);
        alertDialog.setTitle("Update Status");
        alertDialog.setMessage("Please choose: ");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.update_status_layout, null);

        statusGroup = (RadioGroup) findViewById(R.id.status);
        alertDialog.setView(view);

        final String key_STAT = key;

//TODO

//         find the radiobutton by returned id



        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(statusGroup.getCheckedRadioButtonId()));
                orderPlaced.child(key_STAT).setValue(item);
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
                    Notification notification = new Notification("OUI", "Your order " + key + "was updated");
                    Sender sender = new Sender(token.getToken(), notification);

                    remoteAPIService.sendNotice(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if(response.body().success == 1){
                                Toast.makeText(OrdersPlacedActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                finish();
                            } else{
                                Toast.makeText(OrdersPlacedActivity.this, "Error something happened!", Toast.LENGTH_SHORT).show();

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


}
