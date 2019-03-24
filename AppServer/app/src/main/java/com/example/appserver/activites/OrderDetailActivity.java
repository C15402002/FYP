package com.example.appserver.activites;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appserver.R;
import com.example.appserver.adapter.ExtraDetailAdapter;
import com.example.appserver.control.Control;

public class OrderDetailActivity extends AppCompatActivity {

    TextView orderId, status, orderPrice, orderTable, orderComment, orderPayment;

    String order_Id = "";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = findViewById(R.id.orderNum);
        status = findViewById(R.id.status);
        orderTable = findViewById(R.id.tablenum);
        orderPrice = findViewById(R.id.price);
        orderComment = findViewById(R.id.note);
        orderPayment = findViewById(R.id.payment);

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
        recyclerView = findViewById(R.id.listdetails);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent()!= null){
            order_Id = getIntent().getStringExtra("OrderId");

        }
        orderId.setText(order_Id);
        status.setText(Control.currentOrder.getStatus());
        orderTable.setText(Control.currentOrder.getTable());
        orderPrice.setText(Control.currentOrder.getTotal());
        orderComment.setText(Control.currentOrder.getNotes());
//        orderPayment.setText(Control.currentOrder.getPaymentProcess());


        ExtraDetailAdapter adapter = new ExtraDetailAdapter(Control.currentOrder.getListOfOrderPlaced());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
