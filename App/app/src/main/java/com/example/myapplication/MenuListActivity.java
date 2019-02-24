package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.holder.MenuHolder;
import com.example.myapplication.model.Menu;
import com.example.myapplication.view.ProductClickedListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MenuListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference menu;
    FirebaseRecyclerOptions<Menu> options;
    FirebaseRecyclerAdapter<Menu, MenuHolder> adapter;

    String product_TypeId = "";
    public static int restNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        db = FirebaseDatabase.getInstance();
        menu = db.getReference().child("Menu");

        recyclerView = findViewById(R.id.recycleViewFood);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() !=null){
            product_TypeId = getIntent().getStringExtra("Product_TypeId");
        }
        if(product_TypeId!=null&&!product_TypeId.isEmpty()  ){

            options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(menu.orderByChild("productTypeId").equalTo(product_TypeId), Menu.class).build();
            adapter = new FirebaseRecyclerAdapter<Menu, MenuHolder>(options){
                @Override
                protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull Menu model) {
                    Picasso.get().load(model.getImage()).into(holder.fdImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getApplicationContext(),"Could not get message", Toast.LENGTH_LONG).show();

                        }
                    });
                    final Menu clicked = model;
                    holder.setItemClickListener(new ProductClickedListener() {
                        @Override
                        public void onClick(View v, int pos, boolean isLongClicked) {

                        Toast.makeText(MenuListActivity.this, "" + clicked.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MenuListActivity.this, MenuDetailActivity.class);
                        intent.putExtra("MenuId ", adapter.getRef(pos).getKey());
                        startActivity(intent);
                        }
                    });
                    holder.fdName.setText(model.getName());
                }
                @NonNull
                @Override
                public MenuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_items,viewGroup,false);
                    return new MenuHolder(v);
                }

            };
            adapter.startListening();
            recyclerView.setAdapter(adapter);
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
