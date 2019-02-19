package com.example.angelapeng.oui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.angelapeng.oui.holder.MenuHolder;
import com.example.angelapeng.oui.model.Menu;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

public class MenuDetailActivity extends AppCompatActivity {

    TextView fdname, description, price;
    ImageView fdimage;
    FloatingActionButton cart;
    ElegantNumberButton quantity;
    CollapsingToolbarLayout collapsingToolbarLayout;

    String menuId = "";

    FirebaseDatabase menudb;
    DatabaseReference mRef;

    FirebaseRecyclerOptions<Menu> options;
    FirebaseRecyclerAdapter<Menu, MenuHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        menudb = FirebaseDatabase.getInstance();
        mRef = menudb.getReference("Menu");


        fdname = findViewById(R.id.foodname);
        description = findViewById(R.id.description);
        price = findViewById(R.id.foodprice);
        fdimage = findViewById(R.id.foodimage);
        quantity = findViewById(R.id.counter);
        cart = findViewById(R.id.fab);

        collapsingToolbarLayout = findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingToolbar);


        if (getIntent() != null) {
            menuId = getIntent().getStringExtra("MenuId");
        }
        if ( menuId!=null && !menuId.isEmpty()) {
            getDetails(menuId);
        }
    }


    private void getDetails(String menuId) {
        mRef.child(menuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Menu menu = dataSnapshot.getValue(Menu.class);

                Picasso.get().load(menu.getImage()).into(fdimage);
                collapsingToolbarLayout.setTitle(menu.getName());
                price.setText(menu.getPrice());
                fdname.setText(menu.getName());
                description.setText(menu.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

