package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import androidx.appcompat.app.AppCompatActivity;

public class ScanActivity extends AppCompatActivity {
    //opens camera
    Button scan_btn;


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



    }

    private void scanQR() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan the QR");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mScannerView.stopCamera();
    }

    protected void onActivityResult(int requestCode, int grantResults, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, grantResults, data);


        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "scan cancelled", Toast.LENGTH_LONG).show();

            }
            else {
                //parse decoded qrcode's url to open on browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(browserIntent);
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

            }

        }else {
            super.onActivityResult(requestCode, grantResults, data);

        }

    }
}
