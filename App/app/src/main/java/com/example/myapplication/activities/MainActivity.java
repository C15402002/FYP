package com.example.myapplication.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.helper.LocalHelper;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnSignup;
    ImageButton btnlanguage;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        hashKey();

        btnLogin = findViewById(R.id.btnUserLogin);
        btnSignup = findViewById(R.id.btnUserSignup);
        btnlanguage = findViewById(R.id.btnLanguage);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        btnlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguageDialog();
            }
        });

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));
    }

    private void hashKey() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.myapplication", PackageManager.GET_SIGNATURES);

            for(Signature signature:info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void changeLanguageDialog() {
        final String[] listLang = {"English","Espanol", "中文"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(getResources().getString(R.string.lang));
        dialog.setSingleChoiceItems(listLang, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    Paper.book().write("language", "en");
                    updateLanguage((String)Paper.book().read("language"));
                }
                if(i == 1){
                    Paper.book().write("language", "es");
                    updateLanguage((String)Paper.book().read("language"));
                }
                if(i == 2){
                    Paper.book().write("language","zh");
                    updateLanguage((String)Paper.book().read("language"));
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        btnLogin.setText(resources.getString(R.string.btnLogin));
        btnSignup.setText(resources.getString(R.string.btnSignup));
        //btnlanguage.setText(resources.getString(R.string.language));
    }

}

