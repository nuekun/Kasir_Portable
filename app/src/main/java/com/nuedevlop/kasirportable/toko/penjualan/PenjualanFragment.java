package com.nuedevlop.kasirportable.toko.penjualan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nuedevlop.kasirportable.ProdukActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.ProdukAdapter;
import com.nuedevlop.kasirportable.utils.database.Produk;


public class PenjualanFragment extends Fragment {

    private View view;
    private Context context;
    Button btnProses;
    ImageButton btnTambah;
    private final int RecKodeProduk = 210;


    public PenjualanFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_penjualan, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();

        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProdukActivity.class);
            startActivityForResult(intent, RecKodeProduk);
        });


    }

    private void init() {
        btnProses = view.findViewById(R.id.btnPenjualanProses);
        btnTambah = view.findViewById(R.id.btnPenjualanTambah);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RecKodeProduk && resultCode == ProdukAdapter.KodeProduk) {

            Produk produk = new Produk();
            produk = data.getParcelableExtra(ProdukAdapter.ExtraProduk);

            Toast.makeText(context, produk.getNama(), Toast.LENGTH_SHORT).show();

        }

    }
}


