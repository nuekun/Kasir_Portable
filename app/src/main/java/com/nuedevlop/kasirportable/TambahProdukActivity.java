package com.nuedevlop.kasirportable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.Update;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nuedevlop.kasirportable.utils.database.Jenis;
import com.nuedevlop.kasirportable.utils.database.JenisDAO;
import com.nuedevlop.kasirportable.utils.database.JenisDB;
import com.nuedevlop.kasirportable.utils.database.Produk;
import com.nuedevlop.kasirportable.utils.database.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.ProdukDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TambahProdukActivity extends AppCompatActivity {

    ImageButton btnBarcode ,btnJenis;
    Button btnSimpan,btnUpdate;
    Spinner spinJenis;
    TextInputEditText txtNama,txtBarcode,txtSuplier,txtBeli,txtJual,txtStok,txtKeterangan;
    private final int REQUEST_CODE = 110;
    JenisDAO jenisDAO;
    ProdukDAO produkDao;
    Jenis jenis;
    public static final String EXTRA_PRODUK = "produk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);
        init();

        btnBarcode.setOnClickListener(v -> ambilBarcode());
        btnJenis.setOnClickListener(v->editJenis());
        btnSimpan.setOnClickListener(v->simpanProduk());


        Produk produk = getIntent().getParcelableExtra(EXTRA_PRODUK);
        if(produk!=null){
            btnSimpan.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            txtNama.setText(produk.getNama());
            txtBeli.setText(String.valueOf(produk.getHargaBeli()));
            txtStok.setText(String.valueOf(produk.getStok()));
            txtJual.setText(String.valueOf(produk.getHargaJual()));
            txtKeterangan.setText(produk.getKeterangan());
            txtBarcode.setText(produk.getBarcode());
            txtSuplier.setText(produk.getSuplier());
            btnUpdate.setOnClickListener(v->UpdateProduk(produk.getIdProduk()));

        }


        ambilJenis();
    }

    private void UpdateProduk(int idProduk) {

        String nama,barcode,suplier,jenis,keterangan;
        int hargaBeli,hargaJual,stok;
        nama = Objects.requireNonNull(txtNama.getText()).toString();
        barcode = Objects.requireNonNull(txtBarcode.getText()).toString();
        suplier = Objects.requireNonNull(txtSuplier.getText()).toString();
        jenis = spinJenis.getSelectedItem().toString();
        keterangan = Objects.requireNonNull(txtKeterangan.getText()).toString();
        if(Objects.requireNonNull(txtBeli.getText()).toString().length()==0){
            txtBeli.setText("0");
        }
        hargaBeli = Integer.parseInt(txtBeli.getText().toString());
        if(Objects.requireNonNull(txtJual.getText()).toString().length()==0){
            txtJual.setText("0");
        }
        hargaJual = Integer.parseInt(txtJual.getText().toString());
        if(Objects.requireNonNull(txtStok.getText()).toString().length()==0){
            txtStok.setText("0");
        }
        stok = Integer.parseInt(txtStok.getText().toString());

        if(jenis.contains("Tambahkan jenis")){
            editJenis();
        }


        if (nama.length()==0){
            Toast.makeText(this, "masukkan nama produk!", Toast.LENGTH_SHORT).show();
        }else{
            try {

                Produk produk = new Produk();
                produk.setIdProduk(idProduk);
                produk.setNama(nama);
                produk.setBarcode(barcode);
                produk.setSuplier(suplier);
                produk.setJenis(jenis);
                produk.setKeterangan(keterangan);
                produk.setHargaJual(hargaJual);
                produk.setHargaBeli(hargaBeli);
                produk.setStok(stok);
                produkDao.insert(produk);
                Intent intent = new Intent(this,ProdukActivity.class);
                startActivity(intent);
                finish();

            }catch (SQLiteConstraintException e){
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

    private void simpanProduk() {

        String nama,barcode,suplier,jenis,keterangan;
        int hargaBeli,hargaJual,stok;
        nama = Objects.requireNonNull(txtNama.getText()).toString();
        barcode = Objects.requireNonNull(txtBarcode.getText()).toString();
        suplier = Objects.requireNonNull(txtSuplier.getText()).toString();
        jenis = spinJenis.getSelectedItem().toString();
        keterangan = Objects.requireNonNull(txtKeterangan.getText()).toString();
        if(Objects.requireNonNull(txtBeli.getText()).toString().length()==0){
            txtBeli.setText("0");
        }
        hargaBeli = Integer.parseInt(txtBeli.getText().toString());
        if(Objects.requireNonNull(txtJual.getText()).toString().length()==0){
            txtJual.setText("0");
        }
        hargaJual = Integer.parseInt(txtJual.getText().toString());
        if(Objects.requireNonNull(txtStok.getText()).toString().length()==0){
            txtStok.setText("0");
        }
        stok = Integer.parseInt(txtStok.getText().toString());

        if(jenis.contains("Tambahkan jenis")){
            editJenis();
        }


        if (produkDao.getCountByBarcode(barcode)> 0 && barcode.length()>0){
            Toast.makeText(this, "Barcode "+barcode+" sudah ada!", Toast.LENGTH_SHORT).show();
        }else if(produkDao.getCountByNama(nama)> 0 && nama.length()>0){
            Toast.makeText(this, "Produk dengan nama "+nama+" sudah ada!",Toast.LENGTH_LONG).show();
        }else if(nama.length()==0){
            Toast.makeText(this, "masukkan nama produk!", Toast.LENGTH_SHORT).show();
        }else{
            try {

                Produk produk = new Produk();
                produk.setNama(nama);
                produk.setBarcode(barcode);
                produk.setSuplier(suplier);
                produk.setJenis(jenis);
                produk.setKeterangan(keterangan);
                produk.setHargaJual(hargaJual);
                produk.setHargaBeli(hargaBeli);
                produk.setStok(stok);
                produkDao.insert(produk);
                finish();

            }catch (SQLiteConstraintException e){
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void init() {

        jenisDAO = Room.databaseBuilder(this, JenisDB.class, "produk")
                .allowMainThreadQueries()
                .build()
                .getJenisDAO();

        produkDao = Room.databaseBuilder(this, ProdukDB.class,"Produk")
                .allowMainThreadQueries()
                .build()
                .getProdukDAO();

        btnUpdate = findViewById(R.id.btnTambahProdukUpdate);
        txtBeli = findViewById(R.id.txtTambahProdukBeli);
        txtJual = findViewById(R.id.txtTambahProdukJual);
        txtStok= findViewById(R.id.txtTambahProdukStok);
        txtKeterangan = findViewById(R.id.txtTambahProdukKeterangan);
        btnSimpan =findViewById(R.id.btnTambahProdukSimpan);
        btnBarcode = findViewById(R.id.btnTambahProdukScan);
        txtNama = findViewById(R.id.txtTambahProdukNama);
        txtBarcode = findViewById(R.id.txtTambahProdukBarcode);
        txtSuplier = findViewById(R.id.txtTambahProdukSuplier);
        btnJenis = findViewById(R.id.btnTambahProdukEditJenis);
        spinJenis = findViewById(R.id.spinTambahProdukJenis);
    }

    private void ambilJenis() {


        ArrayList<String> kategori = new ArrayList<>();
        List<Jenis> list = jenisDAO.getAllJenis();
        if(list.size()!=0) {
            for (int i = 0; i < list.size(); i++) {
                kategori.add(list.get(i).getJenisProduk());
            }
            spinJenis.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategori));
        }
    }

    private void editJenis() {
        TextInputEditText txtJenis;
        ImageButton btnHapus;
        Button btnBatal,btnSimpan;

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_edit_jenis, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnBatal = view.findViewById(R.id.btnEditJenisBatal);
        btnSimpan = view.findViewById(R.id.btnEditJenisSimpan);
        btnHapus = view.findViewById(R.id.btnEditJenisHapus);
        txtJenis = view.findViewById(R.id.txtEditJenis);

        btnBatal.setOnClickListener(v->dialog.dismiss());

        btnHapus.setOnClickListener(v->{
            if (txtJenis.getText().length()!= 0) {
                hapusJenis(txtJenis.getText().toString());
                dialog.dismiss();
            } else {
                Toast.makeText(this, "silahkan masukkan jenis!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSimpan.setOnClickListener(v->{
           if (txtJenis.getText().length()!= 0) {
                simpanJenis(txtJenis.getText().toString());
                dialog.dismiss();
            } else{
               Toast.makeText(this, "silahkan masukkan jenis!", Toast.LENGTH_SHORT).show();
           }
        });

        dialog.show();

    }

    private void hapusJenis(String string) {

        try {
            if (jenisDAO.getIdCount(string)!=0){
                jenisDAO.deleteJenis(string);
            }else {
                Toast.makeText(this, "Jenis Produk tidak di temukan!", Toast.LENGTH_SHORT).show();
            }


        }catch (SQLiteConstraintException e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        ambilJenis();
    }

    private void simpanJenis(String string) {

        try {
            if (jenisDAO.getIdCount(string)==0){
                jenis = new Jenis();
                jenis.setJenisProduk(string);
                jenisDAO.insert(jenis);
            }else {
                Toast.makeText(this, "Jenis Produk sudah ada!", Toast.LENGTH_SHORT).show();
            }


        }catch (SQLiteConstraintException e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

         ambilJenis();

    }

    private void ambilBarcode() {
        Intent intent = new Intent(this,ScanActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;


        if (requestCode == REQUEST_CODE && resultCode == ScanActivity.RESULT_CODE) {

            String barcode = data.getStringExtra("barcode");
            txtBarcode.setText(barcode);

        }
    }

}
