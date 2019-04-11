package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.helper.LocalHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.myapplication.control.Control;
import com.example.myapplication.database.Database;
import com.example.myapplication.model.Menu;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MenuDetailActivity extends AppCompatActivity implements RatingDialogListener {

    TextView fdname, description, price;
    ImageView fdimage;
    FloatingActionButton btnrate;
    Button add, seeReviews;
    ElegantNumberButton quantity;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;

    String menuId = "";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference menu;
    DatabaseReference reviews;

    Menu currentMenu;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        menu =firebaseDatabase.getReference("Restaurant").child(Control.restID).child("details").child("Menu");
        reviews = firebaseDatabase.getReference("Restaurant").child(Control.restID).child("details").child("Reviews");

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


        fdname = findViewById(R.id.foodname);
        description = findViewById(R.id.description);
        price = findViewById(R.id.foodprice);
        fdimage = findViewById(R.id.foodimage);
        ratingBar = findViewById(R.id.ratingStar);

        quantity = findViewById(R.id.counter);

        seeReviews = findViewById(R.id.reviewBtn);
        seeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuDetailActivity.this, SeeReviewsActivity.class);
                intent.putExtra(Control.Review_DishesID, menuId);
                startActivity(intent);

            }
        });

        add = findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToBasket(new Order(Control.currentUser.getPhone(),
                        menuId,
                        Control.restID,
                        currentMenu.getName(),
                        quantity.getNumber(),
                        currentMenu.getPrice()
                ));
                Toast.makeText(MenuDetailActivity.this, getResources().getString(R.string.addBask), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        //add.

        btnrate = findViewById(R.id.rate_btn);
        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showReview();
            }
        });


        collapsingToolbarLayout = findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingToolbar);


        if (getIntent() != null) {
            menuId = getIntent().getStringExtra("MenuId");
        }
        if (menuId!=null && !menuId.isEmpty()) {
            if(Control.checkConnectivity(getBaseContext())) {
                getMenu(menuId);
                getReview(menuId);
            }else{
                Toast.makeText(this, getResources().getString(R.string.interCon), Toast.LENGTH_SHORT).show();
            }
        }

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));

    }

    private void getMenu(String menuId){
        menu.child(menuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentMenu = dataSnapshot.getValue(Menu.class);
                Picasso.get().load(currentMenu.getImage()).into(fdimage);
                collapsingToolbarLayout.setTitle(currentMenu.getName());
                price.setText(currentMenu.getPrice());
                fdname.setText(currentMenu.getName());
                description.setText(currentMenu.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getReview(String menuId) {
        com.google.firebase.database.Query dishReview = reviews.orderByChild("menuId").equalTo(menuId);
        dishReview.addValueEventListener(new ValueEventListener() {
            int count =0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Review dish = postSnapShot.getValue(Review.class);
                    sum += Integer.parseInt(dish.getRate());
                    count++;

                }
                if(count != 0 ) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showReview() {
        new AppRatingDialog.Builder().setPositiveButtonText(getResources().getString(R.string.submit))
                .setNegativeButtonText(getResources().getString(R.string.cancel))
                .setNoteDescriptions(Arrays.asList((getResources().getString(R.string.poor)),(getResources().getString(R.string.notG)),
                        (getResources().getString(R.string.Meh)),(getResources().getString(R.string.great)),(getResources().getString(R.string.amaze))) )
                .setDefaultRating(1).setTitle(getResources().getString(R.string.review))
                .setDescription(getResources().getString(R.string.reviewDesc))
                .setHint(getResources().getString(R.string.reviewHint))
                .setHintTextColor(R.color.grey)
                .setCommentTextColor(android.R.color.black)
                .setCommentBackgroundColor(R.color.white)
                .setWindowAnimation(R.style.RatingAnim)
                .setTitleTextColor(R.color.colorAccent)
                .setDescriptionTextColor(R.color.colorAccent)
            .create(MenuDetailActivity.this).show();

    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {

        final Review review = new Review(Control.currentUser.getName(),menuId,String.valueOf(i),s,Control.restID);

        reviews.push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              Toast.makeText(MenuDetailActivity.this, getResources().getString(R.string.thank), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        add.setText(resources.getString(R.string.addBTN));
        seeReviews.setText(resources.getString(R.string.reviewBTN));
        // phone_input.setText(resources.getString(R.string.phone));
        // password_input.setText(resources.getString(R.string.password));
    }
}



