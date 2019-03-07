package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.control.Control;
import com.example.myapplication.holder.MenuHolder;
import com.example.myapplication.holder.ProductHolder;
import com.example.myapplication.model.Menu;
import com.example.myapplication.model.Product_Type;
import com.example.myapplication.view.ProductClickedListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase db;
    DatabaseReference productList;
    FirebaseRecyclerOptions<Menu> options;
    FirebaseRecyclerAdapter<Menu, MenuHolder> adapter;
    ElegantNumberButton quantity;

    String product_typeId = "";

    FirebaseRecyclerOptions<com.example.myapplication.model.Menu> search_options;
    FirebaseRecyclerAdapter<com.example.myapplication.model.Menu, MenuHolder> search_adapter;
    List<String> recents = new ArrayList<>();

    MaterialSearchBar materialSearchBar;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        db = FirebaseDatabase.getInstance();
        productList = db.getReference("Menu");

        recyclerView = findViewById(R.id.recycleViewFood);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton) findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        materialSearchBar = (MaterialSearchBar)findViewById(R.id.search);
        materialSearchBar.setHint("Search dishes");
        materialSearchBar.setSpeechMode(true);

        getRecents();
        materialSearchBar.setLastSuggestions(recents);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggestion = new ArrayList<String>();
                for(String search:recents){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggestion.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggestion);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        if(getIntent() !=null){
            product_typeId = getIntent().getStringExtra("Product_TypeId");
        }
        if(product_typeId!=null && !product_typeId.isEmpty()  ){
            if(Control.checkConnectivity(getBaseContext())) {

                //select * from menu where foodid = producttypeid
                Query query = productList.orderByChild("foodId").equalTo(product_typeId);

                options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(query, Menu.class).build();
                adapter = new FirebaseRecyclerAdapter<Menu, MenuHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull Menu model) {

                        Picasso.get().load(model.getImage()).into(holder.fdImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                // Toast.makeText(getApplicationContext(), "Could not get message", Toast.LENGTH_LONG).show();

                            }
                        });
                        holder.fdName.setText(model.getName());
                        holder.fdDescript.setText(model.getDescription());
                        holder.fdPrice.setText(String.format("€ %s", model.getPrice().toString()));

                        //final Menu clicked = model;
                        holder.setItemClickListener(new ProductClickedListener() {
                            @Override
                            public void onClick(View v, int pos, boolean isLongClicked) {

                                //Toast.makeText(MenuListActivity.this, "" + clicked.getName(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MenuListActivity.this, MenuDetailActivity.class);
                                intent.putExtra("MenuId", adapter.getRef(pos).getKey());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MenuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_items, viewGroup, false);
                        return new MenuHolder(v);
                    }

                };
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }else{
                Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSearch(CharSequence text) {
        Query query = productList.orderByChild("name").equalTo(text.toString());

        search_options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(query, Menu.class).build();
        search_adapter = new FirebaseRecyclerAdapter<Menu, MenuHolder>(search_options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuHolder menuHolder, int i, @NonNull Menu menu) {

                Picasso.get().load(menu.getImage()).into(menuHolder.fdImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        // Toast.makeText(getApplicationContext(), "Could not get message", Toast.LENGTH_LONG).show();

                    }
                });
                menuHolder.fdName.setText(menu.getName());
                menuHolder.fdDescript.setText(menu.getDescription());
                menuHolder.fdPrice.setText(String.format("€ %s", menu.getPrice().toString()));
                menuHolder.setItemClickListener(new ProductClickedListener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClicked) {

                        //Toast.makeText(MenuListActivity.this, "" + clicked.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MenuListActivity.this, MenuDetailActivity.class);
                        intent.putExtra("MenuId", search_adapter.getRef(pos).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items, parent, false);
                return new MenuHolder(v);
            }
        };
        recyclerView.setAdapter(search_adapter);
    }

    private void getRecents() {
        productList.orderByChild("Product_TypeId").equalTo(product_typeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    Product_Type product = post.getValue(Product_Type.class);
                    recents.add(product.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
