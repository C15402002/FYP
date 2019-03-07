package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ScanActivity extends AppCompatActivity {
    //opens camera
    Button scan_btn;

    FirebaseDatabase fireDB;
    DatabaseReference scannerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan_btn = findViewById(R.id.scanBtn);

        final Activity activity = this;
        Intent intent = getIntent();


        //Zxing library barcode scanner
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    protected void onActivityResult(int requestCode, int grantResults, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, grantResults, data);


        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "scan cancelled", Toast.LENGTH_LONG).show();

            }
            else {
                Intent browserIntent = new Intent(ScanActivity.this, MenuListActivity.class);
               // browserIntent.putExtra("restId")
                startActivity(browserIntent);
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

            }

        }else {
            super.onActivityResult(requestCode, grantResults, data);

        }

    }
}
