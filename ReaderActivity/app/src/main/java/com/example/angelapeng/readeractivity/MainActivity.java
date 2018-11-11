package com.example.angelapeng.readeractivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView)findViewById(R.id.result);
    }

    public void scanBarcode(View v){
        Intent intent = new Intent(this, scannerActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onActivityScanner(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    result.setText(("barcode value: " + barcode.displayValue));
                } else{
                    result.setText("nothing found");
                }
            }

        }else{

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
