package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.holder.ProductHolder;
import com.example.myapplication.holder.RestaurantHolder;
import com.example.myapplication.model.Restaurant;
import com.example.myapplication.view.ProductClickedListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RestaurantsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Restaurant> options;
    FirebaseRecyclerAdapter<Restaurant, RestaurantHolder> adapter;

   //public static int restID;

    String restaurantId = "";
    FirebaseDatabase db;
    DatabaseReference restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        db = FirebaseDatabase.getInstance();
        restaurant = db.getReference().child("Restaurant");

        Intent intent = getIntent();
        //TODO
//        restaurantId = (String) intent.getStringExtra( ScanActivity.QRCODE);
        recyclerView = findViewById(R.id.restList);
        recyclerView.setHasFixedSize(true);

        if(getIntent() !=null){
           restaurantId = getIntent().getStringExtra("RestaurantId");
        }
        if(restaurantId!=null) {
            //TODO
            Query query = restaurant.orderByChild("RestaurantId").equalTo(restaurantId);
            options = new FirebaseRecyclerOptions.Builder<Restaurant>().setQuery(query, Restaurant.class).build();
            adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull RestaurantHolder holder, int position, @NonNull Restaurant model) {

                    Picasso.get().load(model.getImage()).into(holder.restImage, new Callback() {
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
                            Intent intent = new Intent(RestaurantsActivity.this, HomeActivity.class);
                            Control.Restaurant_Scanned = adapter.getRef(pos).getKey();
                            startActivity(intent);
                        }
                    });
                    holder.restName.setText(model.getName());
                }

                @NonNull
                @Override
                public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_items, viewGroup, false);
                    return new RestaurantHolder(v);
                }


            };
        }
            //TODO ERROR HERE
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(adapter);
            adapter.startListening();


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
