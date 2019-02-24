package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.model.Menu;
import com.example.myapplication.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MenuDetailActivity extends AppCompatActivity {

    TextView fdname, description, price;
    ImageView fdimage;
    FloatingActionButton add;
    ElegantNumberButton quantity;
    CollapsingToolbarLayout collapsingToolbarLayout;

    String menuId = "";

    FirebaseDatabase menudb;
    DatabaseReference mRef;

    Menu currentMenu;


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
        add = findViewById(R.id.fab);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToBasket(new Order(
                        //TODO: GET MENU FROM MENUDETAILACTIVITY
                        menuId, currentMenu.getName(),
                        quantity.getNumber(),
                        currentMenu.getPrice()
                ));
                Toast.makeText(MenuDetailActivity.this, "Added to order", Toast.LENGTH_SHORT).show();
            }
        });


        collapsingToolbarLayout = findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingToolbar);


        if (getIntent() != null) {
            menuId = getIntent().getStringExtra("MenuId");
        }
        if (menuId!=null && !menuId.isEmpty()) {
            getMenu(menuId);
        }
    }

    private void getMenu(String menuId){
        mRef.child(menuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//TODO: VIEW
                currentMenu = dataSnapshot.getValue(Menu.class);
                Picasso.get().load(Control.currentMenu.getImage()).into(fdimage);
                collapsingToolbarLayout.setTitle(Control.currentMenu.getName());
                price.setText(Control.currentMenu.getPrice());
                fdname.setText(Control.currentMenu.getName());
                description.setText(Control.currentMenu.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}



