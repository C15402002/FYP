package com.example.appserver.activites;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appserver.R;
import com.example.appserver.control.Control;
import com.example.appserver.holder.ReviewHolder;
import com.example.appserver.model.Review;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference review;

    String menuId = "";

    FirebaseRecyclerAdapter<Review, ReviewHolder> adapter;
    FirebaseRecyclerOptions<Review> options;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        firebaseDatabase = FirebaseDatabase.getInstance();
        review = firebaseDatabase.getReference("Restaurant").child(Control.currentUser.getRestId()).child("details").child("Reviews");
        recyclerView = findViewById(R.id.reviewList);
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
        menuId = getIntent().getStringExtra(Control.Review_DishesID);
        Query query = review.orderByChild("menuId").equalTo(menuId);

                options = new FirebaseRecyclerOptions.Builder<Review>().setQuery(query, Review.class).build();
                adapter = new FirebaseRecyclerAdapter<Review, ReviewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i, @NonNull Review review) {
                        reviewHolder.stars.setRating(Float.parseFloat(review.getRate()));
                        reviewHolder.review.setText(review.getComment());
                        reviewHolder.identifyUser.setText(review.getName());


                    }

                    @NonNull
                    @Override
                    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.show_review_layout, parent, false);
                        return new ReviewHolder(v);
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
