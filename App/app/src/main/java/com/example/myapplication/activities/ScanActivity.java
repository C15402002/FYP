package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.control.Control;

import com.example.myapplication.helper.LocalHelper;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity  {
    //opens camera implements ZXingScannerView.ResultHandler


    FirebaseDatabase firebaseDatabase;
    DatabaseReference rest;
    TextView scanDesc;
    Button buttonscan;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        buttonscan = findViewById(R.id.scanBtn);

        scanDesc = findViewById(R.id.scanDesc);


        // restaurantId = getIntent().getStringExtra("RestaurantId");
        firebaseDatabase = FirebaseDatabase.getInstance();
        rest = firebaseDatabase.getReference("Restaurant");

        Paper.init(this);
        String lang = Paper.book().read("language");
        if(lang == null){
            Paper.book().write("language", "en");
        }
        updateLanguage((String)Paper.book().read("language"));
        buttonscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR();
            }
        });

    }

    public void scanQR() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt(" ");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //mScannerView.stopCamera();
    }

    protected void onActivityResult(int requestCode, int grantResults, Intent data) {
        super.onActivityResult(requestCode, grantResults, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, grantResults, data);

        if (result.getContents() != null) {
            String restID = result.getContents();
            Toast.makeText(this, getResources().getString(R.string.scanSuc), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Control.Restaurant_Scanned, restID);
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.scanCan), Toast.LENGTH_SHORT).show();

        }
    }



    private void updateLanguage(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        buttonscan.setText(resources.getString(R.string.scanbtn));
        scanDesc.setText(resources.getString(R.string.scanDesc));
    }
}