package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.holder.MenuHolder;
import com.example.myapplication.holder.ProductHolder;
import com.example.myapplication.holder.RestaurantHolder;
import com.example.myapplication.model.Menu;
import com.example.myapplication.model.Product_Type;
import com.example.myapplication.model.Restaurant;
import com.example.myapplication.view.ProductClickedListener;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ShareRestaurantActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    FirebaseDatabase db;
    DatabaseReference restaurant;
    FirebaseRecyclerOptions<Restaurant> options;
    FirebaseRecyclerAdapter<Restaurant, RestaurantHolder> adapter;

    RecyclerView recyclerView;


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                shareDialog.show(sharePhotoContent);

            }

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_restaurant);
        callbackManager = new CallbackManager.Factory().create();
        shareDialog = new ShareDialog(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = FirebaseDatabase.getInstance();
        restaurant = db.getReference("Restaurant");


        recyclerView = findViewById(R.id.shareList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        options = new FirebaseRecyclerOptions.Builder<Restaurant>().setQuery(restaurant, Restaurant.class).build();
        adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantHolder>(options) {


            @NonNull
            @Override
            public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.restlist_layout, parent, false);
                return new RestaurantHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull RestaurantHolder restaurantHolder, int i, @NonNull final Restaurant model) {
                Picasso.get().load(model.getImage()).into(restaurantHolder.restImage);
                restaurantHolder.restName.setText(model.getName());

                restaurantHolder.restShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.get().load(model.getImage()).into(target);
                    }
                });

            }


        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
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
