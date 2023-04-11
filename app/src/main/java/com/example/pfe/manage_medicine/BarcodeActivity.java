package com.example.pfe.manage_medicine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfe.JSONParser;
import com.example.pfe.R;
import com.example.pfe.SharedPrefManager;
import com.example.pfe.manage_analyses.AddAnalysisActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class BarcodeActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 1;
    JSONParser parser = new JSONParser();
    private MedicineAdapter.OnItemClickListener mListener;
    String url = "jdbc:mysql://192.168.43.205:3306/healthbuddy";
    //String url = "jdbc:mysql://192.168.1.16:3306/healthbuddy";
    String user = "root";
    String password = "";
    int success, number;
    ProgressDialog dialog;
    String scannedBarcode;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    ArrayList<String> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        //Bundle bundle = getIntent().getExtras();
        //myList = bundle.getStringArrayList("medecinelist");

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
        Switch flashlightSwitch = findViewById(R.id.flashlight);

        flashlightSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> {
            if (isChecked) {
                turnOnFlashlight();
            } else {
                turnOffFlashlight();
            }
        });

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

    private void turnOnFlashlight() {
        if (cameraSource != null) {
            try {
                Field[] declaredFields = CameraSource.class.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.getType() == Camera.class) {
                        field.setAccessible(true);
                        Camera camera = (Camera) field.get(cameraSource);
                        if (camera != null) {
                            Camera.Parameters params = camera.getParameters();
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(params);
                            break;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOffFlashlight() {
        if (cameraSource != null) {
            try {
                Field[] declaredFields = CameraSource.class.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.getType() == Camera.class) {
                        field.setAccessible(true);
                        Camera camera = (Camera) field.get(cameraSource);
                        if (camera != null) {
                            Camera.Parameters params = camera.getParameters();
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(params);
                            break;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
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
                        .setAction("Retry", v -> ActivityCompat.requestPermissions(BarcodeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST)).show();
            }
        }
    }
    private void onBarcodeScanned(Barcode barcode) {
        final String message = "Format: " + barcode.format + "\nValue: " + barcode.rawValue;
        scannedBarcode = barcode.rawValue;
        runOnUiThread(() -> {
            /*if (myList.contains(barcode.rawValue)) {
                Toast.makeText(BarcodeActivity.this, "Medicine existing!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BarcodeActivity.this, InventoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {*/
                Toast.makeText(BarcodeActivity.this, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AddMedicineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("key", barcode.rawValue);
                startActivity(intent);
            //}
        });
    }
    public void goToPreviousActivity(View view) {
        finish();
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
    public void OpenAddMedicine(View view) {
        startActivity(new Intent(BarcodeActivity.this, AddMedicineActivity.class));
    }
}


