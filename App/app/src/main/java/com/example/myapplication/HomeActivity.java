package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.control.Control;
import com.example.myapplication.holder.ProductHolder;
import com.example.myapplication.model.Product_Type;
import com.example.myapplication.model.Token;
import com.example.myapplication.view.ProductClickedListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase db;
    DatabaseReference product;
    TextView email, name;

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Product_Type> options;
    FirebaseRecyclerAdapter<Product_Type, ProductHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateToken(FirebaseInstanceId.getInstance().getToken());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //firebase category
        db = FirebaseDatabase.getInstance();
        product = db.getReference("Product_Type");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(HomeActivity.this, BasketActivity.class);
               startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.viewName);
        name.setText(Control.currentUser.getName());
        email = header.findViewById(R.id.viewEmail);
        email.setText(Control.currentUser.getEmail());

        //get product
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);


        if(Control.checkConnectivity(this)) {

            options = new FirebaseRecyclerOptions.Builder<Product_Type>().setQuery(product, Product_Type.class).build();
            adapter = new FirebaseRecyclerAdapter<Product_Type, ProductHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Product_Type model) {

                    Picasso.get().load(model.getImage()).into(holder.itemImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                          //  Toast.makeText(getApplicationContext(), "Could not get message", Toast.LENGTH_LONG).show();

                        }
                    });
                    //final Menu clicked = model;
                    holder.setItemClickListener(new ProductClickedListener() {
                        @Override
                        public void onClick(View v, int pos, boolean isLongClicked) {

                            // Toast.makeText(HomeActivity.this, "" + clicked.getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, MenuListActivity.class);
                            intent.putExtra("Product_TypeId", adapter.getRef(pos).getKey());
                            startActivity(intent);
                        }
                    });
                    holder.itemName.setText(model.getName());
                }

                @NonNull
                @Override
                public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_items, viewGroup, false);
                    return new ProductHolder(v);
                }


            };

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter.startListening();
            recyclerView.setAdapter(adapter);


        }else{
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateToken(String token) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Tokens");

        Token data = new Token(false, token );
        databaseReference.child(Control.currentUser.getPhone()).setValue(data);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the product; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home,menu);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_menu) {
           onBackPressed();

        }else if (id == R.id.nav_history) {
            Intent intent = new Intent(HomeActivity.this, OrderPlacedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {

        }else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

