package com.example.angelapeng.loginsetup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {
     Button scan_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scan_btn = findViewById(R.id.scan_btn);
        final Activity activity = this;

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
//                String url = result.getContents();
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                // startActivity(i);
            }

        }else {
            super.onActivityResult(requestCode, grantResults, data);

        }

    }
}
