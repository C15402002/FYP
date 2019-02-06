package com.example.angelapeng.loginsetup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {

    //opens camera
    Button scan_btn;

    //view user email on login
    String EmailDisplay;
    TextView Email;

    //logout
    Button LogOutbtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scan_btn = findViewById(R.id.scan_btn);

        final Activity activity = this;

        Email = findViewById(R.id.textView1);
        LogOutbtn = findViewById(R.id.buttonLogout);

        Intent intent = getIntent();

        EmailDisplay = intent.getStringExtra(LoginActivity.userEmail);

        Email.setText(Email.getText().toString() + EmailDisplay);


        LogOutbtn.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                finish();

                Toast.makeText(ScannerActivity.this, "Log Out Successfull", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ScannerActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

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
