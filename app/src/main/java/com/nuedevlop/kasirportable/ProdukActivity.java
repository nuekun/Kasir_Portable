package com.nuedevlop.kasirportable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.nuedevlop.kasirportable.adapter.ProdukAdapter;
import com.nuedevlop.kasirportable.utils.database.jenis.Jenis;
import com.nuedevlop.kasirportable.utils.database.jenis.JenisDAO;
import com.nuedevlop.kasirportable.utils.database.jenis.JenisDB;
import com.nuedevlop.kasirportable.utils.database.produk.Produk;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDB;

import java.util.ArrayList;
import java.util.List;

public class ProdukActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton btnScan,btnCari;
    Button btnKembali,btnTambah;
    private final int REQUEST_CODE = 110;
    RecyclerView recProduk;
    ProdukDAO produkDAO;
    JenisDAO jenisDAO;
    Spinner spinJenis;
    EditText txtCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        init();

        btnScan.setOnClickListener(v -> ambilBarcode());
        btnKembali.setOnClickListener(v->finish());
        btnTambah.setOnClickListener(v->tambahProduk());

        ambilProduk("","");
       // ambilJenis();
        btnCari.setOnClickListener(v->cari());
        txtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cari();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinJenis.setOnItemSelectedListener(this);
        spinJenis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(spinJenis.getSelectedItemPosition()==0){
                ambilJenis();
                return false;}
                else {
                    return false;
                }
            }
        });

    }

    private void cari() {
        String jenis = spinJenis.getSelectedItem().toString();
        if (jenis.equals("Pilih Jenis Produk")){
            ambilProduk("",txtCari.getText().toString());
        }else{
            if(txtCari.getText().equals("")){
                if (spinJenis.getSelectedItem().toString().equals("semua")){
                    ambilProduk("","");
                }
                else {
                    ambilProduk(jenis,"");
                }
            }else{
                if (spinJenis.getSelectedItem().toString().equals("semua")){
                    ambilProduk("",txtCari.getText().toString());
                }
                else {
                    ambilProduk(jenis,txtCari.getText().toString());
                }
            }
        }

    }

    private void ambilJenis() {
        ArrayList<String> kategori = new ArrayList<>();
        List<Jenis> list = jenisDAO.getAllJenis();
        kategori.add("semua");
        if(list.size()!=0) {
            for (int i = 0; i < list.size(); i++) {
                kategori.add(list.get(i).getJenisProduk());
            }
            spinJenis.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategori));
        }
    }

    private void ambilProduk(String jenis, String nama) {

        List<Produk> produks = produkDAO.getProdukByJenisAndNama(jenis,nama);
        RecyclerView recyclerView = findViewById(R.id.recProduk);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ProdukAdapter produkAdapter = new ProdukAdapter(this, produks);
        recyclerView.setAdapter(produkAdapter);


    }

    private void tambahProduk() {
        Intent intent = new Intent(this,TambahProdukActivity.class);
        startActivityForResult(intent,69);
    }

    private void ambilBarcode() {
        Intent intent = new Intent(this,ScanActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void init() {



        produkDAO = Room.databaseBuilder(this, ProdukDB.class,"Produk")
                .allowMainThreadQueries()
                .build()
                .getProdukDAO();

        jenisDAO = Room.databaseBuilder(this, JenisDB.class, "produk")
                .allowMainThreadQueries()
                .build()
                .getJenisDAO();

        btnScan = findViewById(R.id.btnProdukScan);
        btnKembali = findViewById(R.id.btnProdukKembali);
        btnCari = findViewById(R.id.btnProdukCari);
        btnTambah = findViewById(R.id.btnProdukTambah);
        recProduk = findViewById(R.id.recProduk);
        spinJenis = findViewById(R.id.spinProdukJenis);
        txtCari = findViewById(R.id.txtProdukCari);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == ScanActivity.RESULT_CODE) {

            assert data != null;
            String barcode = data.getStringExtra("barcode");
            getProdukbyBarcode(barcode);

        }else{
            ambilProduk("","");

        }
    }

    private void getProdukbyBarcode(String barcode) {

        if(produkDAO.getCountByBarcode(barcode)==0){
            Toast.makeText(this, "Barcode "+barcode+" tidak di temukan!", Toast.LENGTH_SHORT).show();
        }else{
            List<Produk> produks = produkDAO.getProdukbyBarcode(barcode);
            RecyclerView recyclerView = findViewById(R.id.recProduk);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            ProdukAdapter produkAdapter = new ProdukAdapter(this, produks);
            recyclerView.setAdapter(produkAdapter);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       cari();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
