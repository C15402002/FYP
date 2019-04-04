package com.example.myapplication.application;

import android.app.Application;
import android.content.Context;

import com.example.myapplication.helper.LocalHelper;

public class LanguageApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }
}
