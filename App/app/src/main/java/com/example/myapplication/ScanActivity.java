package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.control.Control;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;


import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //opens camera
    Button scan_btn;

    private ZXingScannerView mScannerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan_btn = findViewById(R.id.scanBtn);

        //Zxing library barcode scanner
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR();
            }
        });


        // restaurantId = getIntent().getStringExtra("RestaurantId");
        firebaseDatabase = FirebaseDatabase.getInstance();
        rest = firebaseDatabase.getReference("Restaurant");
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
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//            builder1.setTitle("Scan Result");
//            builder1.setMessage("Please scan QR code on the table.");
//            builder1.setCancelable(true);
//
//            builder1.setPositiveButton(
//                    "Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mScannerView.resumeCameraPreview(ScanActivity.this);
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
        }
    }
}