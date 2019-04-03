package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.R;

public class StartActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH =3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
