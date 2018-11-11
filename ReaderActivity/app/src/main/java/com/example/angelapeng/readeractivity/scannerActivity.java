package com.example.angelapeng.readeractivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.jar.Manifest;

import static android.view.SurfaceHolder.*;

/**
 * Created by angelapeng on 08/11/2018.
 */

public class scannerActivity extends Activity {

    SurfaceView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        preview = (SurfaceView) findViewById(R.id.preview);
        createCameraSource();
    }

    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();
        preview.getHolder().addCallback(Callback() {

            public void surfaceCreated (SurfaceHolder holder){
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(preview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            /*
            public void surfaceChanged (SurfaceHolder holder,int format, int width, int height){


            }

            public void surfaceDestroyed(SurfaceHolder holder){
                cameraSource.stop();

            } */

        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){
            public void release(){

            }
            public void receiveDetections(Detector.Detections<Barcode> detections){
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if(barcode.size()>0){
                    Intent intent = new Intent();
                    intent.putExtra("Barcode", barcode.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS, intent);
                    finish();
            }
        });

        }
    }
}

