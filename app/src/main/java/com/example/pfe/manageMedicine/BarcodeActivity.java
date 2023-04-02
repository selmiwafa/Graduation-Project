package com.example.pfe.manageMedicine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfe.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        surfaceView = findViewById(R.id.surfaceView);

        // Create barcode detector
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        // Add barcode detector listener
        barcodeDetector.setProcessor(new BarcodeDetectorListener());

        // Create camera source
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        // Set surface view callback
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Check camera permission
                if (ContextCompat.checkSelfPermission(BarcodeActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCameraSource();
                } else {
                    ActivityCompat.requestPermissions(BarcodeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopCameraSource();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCameraSource();
    }

    @SuppressLint("MissingPermission")
    private void startCameraSource() {
        try {
            cameraSource.start(surfaceView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopCameraSource() {
        cameraSource.stop();
        cameraSource.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraSource();
            } else {
                Snackbar.make(surfaceView, "Camera permission is required.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BarcodeActivity.this,
                                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                            }
                        })
                        .show();
            }
        }
    }

    private void onBarcodeScanned(Barcode barcode) {
        final String message = "Format: " + barcode.format + "\nValue: " + barcode.rawValue;
        runOnUiThread(() -> Toast.makeText(BarcodeActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private class BarcodeDetectorListener implements Detector.Processor<Barcode> {

        @Override
        public void release() {
        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            SparseArray<Barcode> barcodes = detections.getDetectedItems();
            if (barcodes.size() > 0) {
                onBarcodeScanned(barcodes.valueAt(0));
            }
        }
    }
}


