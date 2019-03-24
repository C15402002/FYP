package com.example.myapplication.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.holder.MenuHolder;
import com.example.myapplication.model.Menu;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SearchFoodsActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter<Menu, MenuHolder> search_adapter;
    List<String> recents = new ArrayList<>();

    MaterialSearchBar materialSearchBar;

    FirebaseRecyclerOptions<Menu> options;
    FirebaseRecyclerAdapter<Menu, MenuHolder> adapter;

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foods);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dishes = firebaseDatabase.getReference("Restaurant").child(Control.restID).child("details").child("Menu");

        recyclerView = findViewById(R.id.recycleSearchAll);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchAll);
        materialSearchBar.setHint("Search dishes");
        materialSearchBar.setSpeechMode(false);

        getRecents();
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
        
        getAllDishes();


    }

    private void getAllDishes() {
        Query query = dishes;

        options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(query, Menu.class).build();
        adapter = new FirebaseRecyclerAdapter<Menu, MenuHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuHolder holder, final int position, @NonNull Menu model) {

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
                        Intent intent = new Intent(SearchFoodsActivity.this, MenuDetailActivity.class);
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
    }

    private void startSearch(CharSequence text) {
        Query query = dishes.orderByChild("name").equalTo(text.toString());

        options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(query, Menu.class).build();
        search_adapter = new FirebaseRecyclerAdapter<Menu, MenuHolder>(options) {
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
                        Intent intent = new Intent(SearchFoodsActivity.this, MenuDetailActivity.class);
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
        search_adapter.startListening();
        recyclerView.setAdapter(search_adapter);
    }

    private void getRecents() {
        dishes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    Menu product = post.getValue(Menu.class);
                    recents.add(product.getName());
                }
                materialSearchBar.setLastSuggestions(recents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }if(search_adapter!= null){
            search_adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }if(search_adapter!= null){
            search_adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }if(search_adapter!= null){
            search_adapter.startListening();
        }
    }
}
