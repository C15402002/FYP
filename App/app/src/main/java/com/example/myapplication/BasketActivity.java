package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.BasketAdapter;
import com.example.myapplication.config.RemoteAPIService;
import com.example.myapplication.control.Control;
import com.example.myapplication.control.Paypal;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class BasketActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST = 9999;
    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public TextView totalPrice;
    Button placeOrder;

    List<Order> listOfOrderPlaced = new ArrayList<>();
    BasketAdapter adapter;

    RemoteAPIService remoteAPIService;

    static PayPalConfiguration palConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                                                        .clientId(Paypal.paypal_ID);
    String tablenum, notes;



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Control.delete)){
            deleteBasket(item.getOrder());
        }
        return true;

    }

    private void deleteBasket(int order) {
        listOfOrderPlaced.remove(order);
        new Database(this).deleteFromBasket(Control.currentUser.getPhone());
        for(Order item:listOfOrderPlaced){
            new Database(this).addToBasket(item);
        }
        loadBasket();
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Restaurant").child(Control.restID).child("OrderPlaced");

        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = new Intent(this, Paypal.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        startService(intent);

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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });



        loadBasket();

    }

    private void loadBasket(){
        listOfOrderPlaced = new Database(this).getOrderBasket(Control.currentUser.getPhone());
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
        final RadioButton payPal = view.findViewById(R.id.Paypal);
        final RadioButton cash = view.findViewById(R.id.CallCash);

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);


        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tablenum = tableEdit.getText().toString();
                notes = commentEdit.getText().toString();


                if(!cash.isChecked() && !payPal.isChecked()){
                    Toast.makeText(BasketActivity.this, "Please select payment method!", Toast.LENGTH_SHORT).show();

                    return;
                }else if(cash.isChecked()){

                    MakeOrder makeOrder = new MakeOrder(Control.currentUser.getPhone(),
                            Control.currentUser.getEmail(),
                            tablenum,
                            Control.currentUser.getName(),
                            totalPrice.getText().toString(),
                            "0",
                            notes,
                            "Unpaid",
                            "Cash",
                            Control.Restaurant_Scanned,
                            listOfOrderPlaced);

                    String order_num = String.valueOf(System.currentTimeMillis());

                    databaseReference.child(order_num).setValue(makeOrder);

                    new Database(getBaseContext()).deleteFromBasket(Control.currentUser.getPhone());

                    notifyServer(order_num);
                    Toast.makeText(BasketActivity.this, "Order sent to kitchen", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(payPal.isChecked()){
                    String formatTotal = totalPrice.getText().toString().replace("€","").replace(",","");
                    float sumTotal = Float.parseFloat(formatTotal);

                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formatTotal), "EUR", "OUI Order", PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                    startActivityForResult(intent, PAYPAL_REQUEST);
                }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (paymentConfirmation != null) {
                    try {
                        String payPalDetail = paymentConfirmation.toJSONObject().toString();
                        JSONObject jsonObject = new JSONObject(payPalDetail);


                        MakeOrder makeOrder = new MakeOrder(Control.currentUser.getPhone(),
                                Control.currentUser.getEmail(),
                                tablenum,
                                Control.currentUser.getName(),
                                totalPrice.getText().toString(),
                                "0",
                                notes,
                                jsonObject.getJSONObject("response").getString("state"),
                                "Paypal",
                                Control.Restaurant_Scanned,
                                listOfOrderPlaced);

                        String order_num = String.valueOf(System.currentTimeMillis());

                        databaseReference.child(order_num).setValue(makeOrder);
                        new Database(getBaseContext()).deleteFromBasket(Control.currentUser.getPhone());
                        notifyServer(order_num);
                        Toast.makeText(BasketActivity.this, "Order sent to kitchen", Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Payment Invalid", Toast.LENGTH_SHORT).show();
            }
        }
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

