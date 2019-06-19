package de.innocow.innocow_v001.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;

public class BarcodeScanner extends AppCompatActivity {

    private final int MIN_LENGTH = 5;
    private SurfaceView cameraSurfaceView;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        cameraSurfaceView = findViewById(R.id.surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeScanningOperation();
    }

    private void initializeScanningOperation() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920, 1080)
                .build();

        cameraSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraSurfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                barcodeDetector.release();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();

                if (barcode.size() > 0 && barcode.valueAt(0).rawValue.length() > MIN_LENGTH) {
                    Intent intent = new Intent(BarcodeScanner.this, CowSearch.class);
                    String searchPhrase = barcode.valueAt(0).rawValue.substring(0,
                            barcode.valueAt(0).rawValue.length()-1);
                    intent.putExtra(StringConstants.SEARCH_PHRASE, searchPhrase);
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }
}
