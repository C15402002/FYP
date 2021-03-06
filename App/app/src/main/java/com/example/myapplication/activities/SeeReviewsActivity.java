package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.helper.LocalHelper;
import com.example.myapplication.holder.MenuHolder;
import com.example.myapplication.holder.ReviewHolder;
import com.example.myapplication.model.Review;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SeeReviewsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference review;

    String menuId = "";

    FirebaseRecyclerAdapter<Review, ReviewHolder> adapter;
    FirebaseRecyclerOptions<Review> options;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_reviews);

        firebaseDatabase = FirebaseDatabase.getInstance();
        review = firebaseDatabase.getReference("Restaurant").child(Control.restID).child("details").child("Reviews");
        recyclerView = findViewById(R.id.seeReviews);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }

        if(getIntent() != null){
            menuId = getIntent().getStringExtra(Control.Review_DishesID);

        }if(!menuId.isEmpty()){
            if (Control.checkConnectivity(getBaseContext())) {
                Query query = review.orderByChild("menuId").equalTo(menuId);

                options = new FirebaseRecyclerOptions.Builder<Review>().setQuery(query, Review.class).build();
                adapter = new FirebaseRecyclerAdapter<Review, ReviewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i, @NonNull Review review) {
                        reviewHolder.stars.setRating(Float.parseFloat(review.getRate()));
                        reviewHolder.review.setText(review.getComment());
                        reviewHolder.identifyUser.setText(Control.currentUser.getName());


                    }

                    @NonNull
                    @Override
                    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.show_review_layout, parent, false);
                        return new ReviewHolder(v);
                    }
                };

                loadReviews(menuId);
            } else {
                Toast.makeText(SeeReviewsActivity.this, getResources().getString(R.string.interCon), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void loadReviews(String menuId) {
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
