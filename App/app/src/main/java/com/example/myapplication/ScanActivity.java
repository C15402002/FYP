package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.holder.RestaurantHolder;
import com.example.myapplication.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //opens camera
    Button scan_btn;

    FirebaseDatabase fireDB;
    DatabaseReference scannerDB;

    public final static String QRCODE = "QRCODE";
    private ZXingScannerView mScannerView;

    FirebaseRecyclerAdapter<Restaurant, RestaurantHolder> adapter;
    FirebaseRecyclerOptions<Restaurant> options;
//    private static final int RC_BARCODE_CAPTURE = 9001;

  //  private IntentIntegrator intentIntegrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan_btn = findViewById(R.id.scanBtn);

        fireDB = FirebaseDatabase.getInstance();
        scannerDB = fireDB.getReference("Restaurant");
       // intentIntegrator = new IntentIntegrator(this);

        //Zxing library barcode scanner
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScannerView = new ZXingScannerView(ScanActivity.this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(ScanActivity.this);
                mScannerView.startCamera();
                //intentIntegrator.initiateScan();
            }
        });
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
                try{
                    JSONObject object = new JSONObject(result.getContents());
                    Intent browserIntent = new Intent(ScanActivity.this, RestaurantsActivity.class);
                    browserIntent.putExtra("RestaurantId", getTaskId());
                    startActivity(browserIntent);
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }else {
            super.onActivityResult(requestCode, grantResults, data);

        }

    }


    @Override
    public void handleResult(Result rawResult) {
        try {
            int restID = Integer.valueOf(rawResult.getText());
            Intent intent = new Intent(this, RestaurantsActivity.class);
            intent.putExtra(QRCODE,restID);
            startActivity(intent);
            finish();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
