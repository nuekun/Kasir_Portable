package com.nuedevlop.kasirportable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProdukActivity extends AppCompatActivity {
    ImageButton btnScan;
    private final int REQUEST_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        init();

        btnScan.setOnClickListener(v -> ambilBarcode());

    }

    private void ambilBarcode() {
        Intent intent = new Intent(this,ScanActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void init() {
        btnScan = findViewById(R.id.btnProdukScan);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE && resultCode == ScanActivity.RESULT_CODE) {

            String barcode = data.getStringExtra("barcode");
            Toast.makeText(this, barcode, Toast.LENGTH_SHORT).show();

        }
    }
}
