package com.nuedevlop.kasirportable.stok;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.ProdukAdapter;
import com.nuedevlop.kasirportable.adapter.StokAdapter;
import com.nuedevlop.kasirportable.utils.database.jenis.Jenis;
import com.nuedevlop.kasirportable.utils.database.jenis.JenisDAO;
import com.nuedevlop.kasirportable.utils.database.jenis.JenisDB;
import com.nuedevlop.kasirportable.utils.database.produk.Produk;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDB;

import java.util.ArrayList;
import java.util.List;


public class StokFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    RecyclerView recProduk;
    ProdukDAO produkDAO;
    JenisDAO jenisDAO;
    Spinner spinJenis;
    EditText txtCari;
    ImageButton btnCari;

    public StokFragment() {

        // Required empty public constructor
    }

    private void ambildata(String jenis, String nama) {

        List<Produk> produks = produkDAO.getProdukByJenisAndNama(jenis,nama);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recProduk.setLayoutManager(llm);
        StokAdapter stokAdapter = new StokAdapter(context,produks);
        recProduk.setAdapter(stokAdapter);

    }

    private void init() {
        produkDAO = Room.databaseBuilder(context, ProdukDB.class,"Produk")
                .allowMainThreadQueries()
                .build()
                .getProdukDAO();

        jenisDAO = Room.databaseBuilder(context, JenisDB.class, "produk")
                .allowMainThreadQueries()
                .build()
                .getJenisDAO();

        btnCari = view.findViewById(R.id.btnStokCari);
        recProduk = view.findViewById(R.id.recStokProduk);
        spinJenis = view.findViewById(R.id.spinStokJenis);
        txtCari = view.findViewById(R.id.txtStokCari);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stok, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();
        ambildata("","");

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

    private void ambilJenis() {
        ArrayList<String> kategori = new ArrayList<>();
        List<Jenis> list = jenisDAO.getAllJenis();
        kategori.add("semua");
        if(list.size()!=0) {
            for (int i = 0; i < list.size(); i++) {
                kategori.add(list.get(i).getJenisProduk());
            }
            spinJenis.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, kategori));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cari();
    }

    private void cari() {
        String jenis = spinJenis.getSelectedItem().toString();
        if (jenis.equals("Pilih Jenis Produk")){
            ambildata("",txtCari.getText().toString());
        }else{
            if(txtCari.getText().equals("")){
                if (spinJenis.getSelectedItem().toString().equals("semua")){
                    ambildata("","");
                }
                else {
                    ambildata(jenis,"");
                }
            }else{
                if (spinJenis.getSelectedItem().toString().equals("semua")){
                    ambildata("",txtCari.getText().toString());
                }
                else {
                    ambildata(jenis,txtCari.getText().toString());
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
