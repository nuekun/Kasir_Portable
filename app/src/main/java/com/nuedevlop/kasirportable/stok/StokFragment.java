package com.nuedevlop.kasirportable.stok;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.ProdukAdapter;
import com.nuedevlop.kasirportable.adapter.StokAdapter;
import com.nuedevlop.kasirportable.utils.database.jenis.JenisDAO;
import com.nuedevlop.kasirportable.utils.database.jenis.JenisDB;
import com.nuedevlop.kasirportable.utils.database.produk.Produk;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDB;

import java.util.List;


public class StokFragment extends Fragment {

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

        List<Produk> produks = produkDAO.getProdukByJenisAndNama("","");
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
