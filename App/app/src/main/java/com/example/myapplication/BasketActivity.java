package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    List<Order> listOfOrderPlaced = new ArrayList<>();
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
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }

        });

        listOfOrderPlaced = new Database(this).getOrderBasket();
        adapter = new BasketAdapter(listOfOrderPlaced, this);
        recyclerView.setAdapter(adapter);

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
        final EditText editText = new EditText(BasketActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(layoutParams);
        alertDialog.setView(editText);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);

        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MakeOrder makeOrder = new MakeOrder(Control.currentUser.getPhone(),
                        Control.currentUser.getName(),
                        Control.currentUser.getEmail(),
                        editText.getText().toString(),
                        totalPrice.getText().toString(),
                        listOfOrderPlaced);



                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(makeOrder);
                new Database(getBaseContext()).deleteFromBasket();
                Toast.makeText(BasketActivity.this, "Order sent to kitchen", Toast.LENGTH_SHORT).show();
                finish();

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

}

