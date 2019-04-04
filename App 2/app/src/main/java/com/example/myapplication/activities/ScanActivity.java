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

import com.example.myapplication.R;
import com.example.myapplication.control.Control;

import com.example.myapplication.helper.LocalHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.zxing.Result;


import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //opens camera
    private ZXingScannerView mScannerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        scanQR();


        // restaurantId = getIntent().getStringExtra("RestaurantId");
        firebaseDatabase = FirebaseDatabase.getInstance();
        rest = firebaseDatabase.getReference("Restaurant");

//        Paper.init(this);
//        String lang = Paper.book().read("language");
//        if(lang == null){
//            Paper.book().write("language", "en");
//        }
//        updateLanguage((String)Paper.book().read("language"));
    }

    public void scanQR() {
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        try {
            String restId = String.valueOf(result.getText());
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Control.Restaurant_Scanned, restId);
            startActivity(intent);
        } catch (Exception e) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Scan Result");
            builder1.setMessage("Please scan QR code on the table.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mScannerView.resumeCameraPreview(ScanActivity.this);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

//    private void updateLanguage(String language) {
//        Context context = LocalHelper.setLocale(this, language);
//        Resources resources = context.getResources();
//
//        scan_btn.setText(resources.getString(R.string.scanbtn));
//        scanDesc.setText(resources.getString(R.string.scanDesc));
//    }
}