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


    String product_typeId = "";

    FirebaseRecyclerOptions<com.example.myapplication.model.Menu> search_options;
    FirebaseRecyclerAdapter<com.example.myapplication.model.Menu, MenuHolder> search_adapter;
    List<String> recents = new ArrayList<>();

   // MaterialSearchBar materialSearchBar;
//


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        db = FirebaseDatabase.getInstance();
        productList = db.getReference("Restaurant").child(Control.restID).child("details").child("Menu");

        recyclerView = findViewById(R.id.recycleSearch);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        if (getIntent() != null) {
            product_typeId = getIntent().getStringExtra("Product_TypeId");
        }
        if (product_typeId != null && !product_typeId.isEmpty()) {
            if (Control.checkConnectivity(getBaseContext())) {

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
            } else {
                Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
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
