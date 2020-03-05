package com.nuedevlop.kasirportable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class ProdukActivity extends AppCompatActivity {
    ImageButton btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        init();

        btnScan.setOnClickListener(v -> {
            Intent intent = new Intent(this,ScanActivity.class);
            startActivity(intent);
        });
    }

    private void init() {
        btnScan = findViewById(R.id.btnProdukScan);
    }
}
