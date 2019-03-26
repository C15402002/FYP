package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.helper.LocalHelper;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
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

    //MaterialSearchBar materialSearchBar;
    CounterFab fab;

    String restId = "";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateToken(FirebaseInstanceId.getInstance().getToken());
//        Intent intent = getIntent();
//        restId= getIntent().getStringExtra(Control.Restaurant_Scanned);

        Intent intent = getIntent();
        Control.restID = intent.getStringExtra(Control.Restaurant_Scanned);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //firebase category
        db = FirebaseDatabase.getInstance();
        product = db.getReference("Restaurant").child(Control.restID).child("details").child("Product_Type");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });
        fab.setCount(new Database(this).getAmount(Control.currentUser.getPhone()));

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
//
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.search_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_search);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchFoodsActivity.class);
                startActivity(intent);

            }
        });

        View header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.viewName);
        name.setText(Control.currentUser.getName());
        email = header.findViewById(R.id.viewEmail);
        email.setText(Control.currentUser.getEmail());

        //.child(db).addSingleValueEventListener(listener);

        //get product
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));
//
//        if (product) {
//            Toast.makeText(this, "Restaurant does not exist", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(HomeActivity.this, ScanActivity.class);
//            startActivity(intent1);
//        }
        if (Control.restID != null && !Control.restID.isEmpty()) {
            if (Control.checkConnectivity(this)) {
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
                                Toast.makeText(getApplicationContext(), "Could not get message", Toast.LENGTH_LONG).show();



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


            } else {
                Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }

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
        getMenuInflater().inflate(R.menu.home,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_scan) {
            Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_history) {
            Intent intent = new Intent(HomeActivity.this, OrderPlacedActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
    if (id == R.id.action_search){
         startActivity(new Intent(HomeActivity.this, SearchFoodsActivity.class));
     }else if (id == R.id.english){
        Paper.book().write("language", "en");
        updateLanguage((String)Paper.book().read("language"));
        }else if (id == R.id.spanish){
        Paper.book().write("language", "es");
        updateLanguage((String)Paper.book().read("language"));
    }
        return super.onOptionsItemSelected(item);
    }

    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();
    }


        @Override
    protected void onPause() {
        super.onPause();
        //mScannerView.stopCamera();
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
        fab.setCount(new Database(this).getAmount(Control.currentUser.getPhone()));
        if(adapter!=null){
            adapter.startListening();
        }
    }

}

