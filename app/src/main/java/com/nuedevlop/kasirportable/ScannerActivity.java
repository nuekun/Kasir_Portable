package com.nuedevlop.kasirportable;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;

import com.nuedevlop.kasirportable.utils.QrCodeAnalyzer;

public class ScannerActivity extends AppCompatActivity {
    TextureView textureView;
    TextView txtbarcode;
    Preview preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        init();
        startCamera();

        textureView.setOnClickListener(v -> {
            if (preview.isTorchOn()){
                preview.enableTorch(false);
            }
            else{
                preview.enableTorch(true);
            }
        });

    }

    private void startCamera() {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(
                        new Size(textureView.getMeasuredWidth(),textureView.getMeasuredHeight()))
                .setLensFacing(CameraX.LensFacing.BACK)
                .build();
        preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(output -> {
            ViewGroup parent = (ViewGroup) textureView.getParent();
            parent.removeView(textureView);
            parent.addView(textureView, 0);
            textureView.setSurfaceTexture(output.getSurfaceTexture());
            updateTransform();
        });

        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder().build();
        ImageAnalysis imageAnalysis =new ImageAnalysis(imageAnalysisConfig);

        QrCodeAnalyzer qrCodeAnalyzer = new QrCodeAnalyzer(barcodes ->{

            for(int i = 0; i < barcodes.size(); i++){
                    txtbarcode.setText(barcodes.get(i).getRawValue());
                }
            return null;

        } );

        imageAnalysis.setAnalyzer(qrCodeAnalyzer);
        CameraX.bindToLifecycle(this, preview, imageAnalysis);

    }

    private void updateTransform() {
        Matrix matrix = new Matrix();
        float width = textureView.getMeasuredWidth()/2f;
        float height = textureView.getMeasuredHeight()/2f;
        int rotationDegrees ;

        int rotation = (int)textureView.getRotation(); //cast to int bc switches don't like floats

        switch(rotation){ //correct output to account for display rotation
            case Surface.ROTATION_0:
                rotationDegrees  = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegrees  = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegrees  = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegrees  = 270;
                break;
            default:
                return;
        }

        matrix.postRotate((float)rotationDegrees, width, height);
        textureView.setTransform(matrix);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Permissions();
    }

    private void init() {
        textureView = findViewById(R.id.camScanner);
        txtbarcode = findViewById(R.id.txtcode);
    }

    private void Permissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                99);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 99) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Permissions();
            }
        }
    }

}
