package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.BasketAdapter;
import com.example.myapplication.config.RemoteAPIService;
import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.model.MakeOrder;
import com.example.myapplication.model.Notification;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.Response;
import com.example.myapplication.model.Sender;
import com.example.myapplication.model.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class BasketActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView totalPrice;
    Button placeOrder;

    List<Order> listOfOrderPlaced = new ArrayList<>();
    BasketAdapter adapter;

    RemoteAPIService remoteAPIService;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Control.delete)){
            deleteBasket(item.getOrder());
        }
        return true;

    }

    private void deleteBasket(int order) {
        listOfOrderPlaced.remove(order);
        new Database(this).deleteFromBasket();
        for(Order item:listOfOrderPlaced){
            new Database(this).addToBasket(item);
        }
        loadBasket();
    }

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
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listOfOrderPlaced.size() > 0) {
                    showAlertDialog();
                }else {
                    Toast.makeText(BasketActivity.this, "Basket is empty, add products", Toast.LENGTH_SHORT).show();
                }
            }

        });


        loadBasket();

    }

    private void loadBasket(){
        listOfOrderPlaced = new Database(this).getOrderBasket();
        adapter = new BasketAdapter(listOfOrderPlaced, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        remoteAPIService = Control.getCloudMessage();

        Locale locale = new Locale("en","IE");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        float total = 0;
        for(Order order:listOfOrderPlaced){
            total += (Float.parseFloat(order.getPrice()))*(Float.parseFloat(order.getQuantity()));
        }

        totalPrice.setText(numberFormat.format(total));



    }
    private void showAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BasketActivity.this);
        alertDialog.setMessage("Please Enter Table Number: ");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.table_comment_layout,null);

        final EditText tableEdit = view.findViewById(R.id.edtTable);
        final EditText commentEdit = view.findViewById(R.id.editComment);

//        final EditText editText = new EditText(BasketActivity.this);
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        editText.setRawInputType(Configuration.KEYBOARD_12KEY);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        editText.setLayoutParams(layoutParams);
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);


        //TODO
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MakeOrder makeOrder = new MakeOrder(Control.currentUser.getPhone(),
                        Control.currentUser.getEmail(),
                        tableEdit.getText().toString(),
                        Control.currentUser.getName(),
                        totalPrice.getText().toString(),
                        commentEdit.getText().toString(),
                        listOfOrderPlaced);

                String order_num = String.valueOf(System.currentTimeMillis());


                databaseReference.child(order_num).setValue(makeOrder);
                new Database(getBaseContext()).deleteFromBasket();
                notifyServer(order_num);
//                Toast.makeText(BasketActivity.this, "Order sent to kitchen", Toast.LENGTH_SHORT).show();
//                finish();


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

    private void notifyServer(final String order_num) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Tokens");
        Query query = databaseReference.orderByChild("isServerToken").equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Token token = postSnapShot.getValue(Token.class);
                    Notification notification = new Notification("OUI","New Order "+order_num);
                    Sender sender = new Sender(token.getToken(), notification);
                    remoteAPIService.sendNotice(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if(response.body().success == 1){
                                Toast.makeText(BasketActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                finish();
                            } else{
                                Toast.makeText(BasketActivity.this, "Error something happened!", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Toast.makeText(BasketActivity.this, "Error something happened!", Toast.LENGTH_SHORT).show();
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

