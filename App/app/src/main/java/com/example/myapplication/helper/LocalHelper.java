package com.example.myapplication.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import kotlin.Suppress;

public class LocalHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context con){
        String lang = getPersistedData(con, Locale.getDefault().getLanguage());
        return setLocale(con, lang);
    }
    public static Context onAttach(Context con, String defLanugage){
        String lang = getPersistedData(con, Locale.getDefault().getLanguage());
        return setLocale(con, lang);
    }

    public static Context setLocale(Context con, String lang) {
        persist(con, lang);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return updatedStrings(con, lang);
        }
        return updatedStringsLegacy(con, lang);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updatedStrings(Context con, String lang) {
        Locale locale = new Locale(lang);
        Locale.getDefault();
        Configuration configuration = con.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return con.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updatedStringsLegacy(Context con, String lang) {
        Locale locale = new Locale(lang);
        Locale.getDefault();
        Resources resources = con.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return con;
    }

    private static void persist(Context con, String lang) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
    }

    private static String getPersistedData(Context con, String language) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        return sharedPreferences.getString(SELECTED_LANGUAGE, language);
    }
}
