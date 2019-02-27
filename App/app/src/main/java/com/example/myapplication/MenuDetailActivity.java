package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

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
                        menuId, currentMenu.getName(),
                        quantity.getNumber(),
                        currentMenu.getPrice()
                ));
                Toast.makeText(MenuDetailActivity.this, "Added to order", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuDetailActivity.this, MenuListActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(MenuDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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
                currentMenu = dataSnapshot.getValue(Menu.class);
                Picasso.get().load(currentMenu.getImage()).into(fdimage);
                collapsingToolbarLayout.setTitle(currentMenu.getName());
                price.setText(currentMenu.getPrice());
                fdname.setText(currentMenu.getName());
                description.setText(currentMenu.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}



