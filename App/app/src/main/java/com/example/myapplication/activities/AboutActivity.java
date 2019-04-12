package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.config.YoutubeConfig;
import com.example.myapplication.helper.LocalHelper;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.nio.channels.AlreadyBoundException;

public class AboutActivity extends YouTubeBaseActivity {

    TextView  textname;
    Button play;
    YouTubePlayerView playerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    private static final String TAG = "AboutActivity";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Log.d(TAG,"onCreate: Starting..");


        textname = findViewById(R.id.aboutName);

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));

        play = findViewById(R.id.play);
        playerView = findViewById(R.id.viewVid);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"onClick: Initialized.");
                youTubePlayer.loadVideo("Z98hXV9GmzY");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"onClick: Initialized failed.");
            }
        };

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: Initializing YouTube Video ...");
                playerView.initialize(YoutubeConfig.getYoutubeApiKey(), onInitializedListener);

            }
        });


    }

    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        textname.setText(resources.getString(R.string.aboutName));

    }

}
