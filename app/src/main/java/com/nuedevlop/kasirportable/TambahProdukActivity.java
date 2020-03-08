package com.nuedevlop.kasirportable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class TambahProdukActivity extends AppCompatActivity {

    ImageButton btnBarcode ,btnJenis;
    Spinner spinJenis;
    TextInputEditText txtNama,txtBarcode,txtSuplier;
    private final int REQUEST_CODE = 110;
    JenisDAO jenisDAO;
    Jenis jenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);
        init();

        btnBarcode.setOnClickListener(v -> ambilBarcode());
        btnJenis.setOnClickListener(v->editJenis());

        jenisDAO = Room.databaseBuilder(this, JenisDB.class, "produk")
                .allowMainThreadQueries()
                .build()
                .getJenisDAO();

        ambilJenis();

    }

    private void ambilJenis() {


        ArrayList<String> kategori = new ArrayList<>();
        List<Jenis> list = jenisDAO.getAllJenis();
        for (int i = 0; i< list.size();i++){
            kategori.add(list.get(i).getJenisProduk());
        }
        spinJenis.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, kategori));
    }

    private void editJenis() {
        TextInputEditText txtJenis;
        ImageButton btnHapus;
        Button btnBatal,btnSimpan;

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");        // set the custom layout
        final View view = getLayoutInflater().inflate(R.layout.dialog_edit_jenis, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnBatal = view.findViewById(R.id.btnEditJenisBatal);
        btnSimpan = view.findViewById(R.id.btnEditJenisSimpan);
        btnHapus = view.findViewById(R.id.btnEditJenisHapus);
        txtJenis = view.findViewById(R.id.txtEditJenis);

        btnBatal.setOnClickListener(v->dialog.dismiss());

        btnHapus.setOnClickListener(v->{
            hapusJenis(txtJenis.getText().toString());
            dialog.dismiss();

        });

        btnSimpan.setOnClickListener(v->{
            simpanJenis(txtJenis.getText().toString());
            dialog.dismiss();
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

    private void init() {
        btnBarcode = findViewById(R.id.btnTambahProdukScan);
        txtNama = findViewById(R.id.txtTambahProdukNama);
        txtBarcode = findViewById(R.id.txtTambahProdukBarcode);
        txtSuplier = findViewById(R.id.txtTambahProdukSuplier);
        btnJenis = findViewById(R.id.btnTambahProdukEditJenis);
        spinJenis = findViewById(R.id.spinTambahProdukJenis);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE && resultCode == ScanActivity.RESULT_CODE) {

            String barcode = data.getStringExtra("barcode");
            txtBarcode.setText(barcode);

        }
    }

}
