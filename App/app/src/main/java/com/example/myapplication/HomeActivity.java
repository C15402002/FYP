package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.myapplication.holder.MenuHolder;
import com.example.myapplication.holder.RestaurantHolder;
import com.example.myapplication.model.Restaurant;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
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


    CounterFab fab;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateToken(FirebaseInstanceId.getInstance().getToken());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Available Restaurants");
        setSupportActionBar(toolbar);

        //firebase category
        db = FirebaseDatabase.getInstance();
        product = db.getReference("Restaurant").child(Control.Restaurant_Scanned).child("details").child("Product_Type");
        ;


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
                finish();
            }
        });

        View header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.viewName);
        name.setText(Control.currentUser.getName());
        email = header.findViewById(R.id.viewEmail);
        email.setText(Control.currentUser.getEmail());

        //get product
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);


        if (Control.checkConnectivity(this)) {


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
        } else if (id == R.id.nav_menu) {
           onBackPressed();

        }else if (id == R.id.nav_history) {
            Intent intent = new Intent(HomeActivity.this, OrderPlacedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nearby) {
            Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
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
     if(id == R.id.action_scan){
         scanQR();
        }
        return super.onOptionsItemSelected(item);
    }
    private void scanQR() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan the QR");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }
    protected void onActivityResult(int requestCode, int grantResults, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, grantResults, data);

        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "scan cancelled", Toast.LENGTH_LONG).show();

            }
            else {
                //parse decoded qrcode's url to open on browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(browserIntent);
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

            }

        }else {
            super.onActivityResult(requestCode, grantResults, data);

        }

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
        if(adapter!=null){
            adapter.startListening();
        }
    }

}

