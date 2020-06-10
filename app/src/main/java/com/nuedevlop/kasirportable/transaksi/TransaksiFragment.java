package com.nuedevlop.kasirportable.transaksi;

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

import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.TransaksiAdapter;
import com.nuedevlop.kasirportable.utils.database.Refrensi;
import com.nuedevlop.kasirportable.utils.database.RefrensiDAO;
import com.nuedevlop.kasirportable.utils.database.RefrensiDB;
import com.nuedevlop.kasirportable.utils.database.Transaksi;
import com.nuedevlop.kasirportable.utils.database.TransaksiDAO;
import com.nuedevlop.kasirportable.utils.database.TransaksiDB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;


public class TransaksiFragment extends Fragment {

    private View view;
    private Context context;
    private RefrensiDAO refrensiDAO;
    private List<Refrensi> refrensiList;
    private DecimalFormat kursIndonesia;
    private DecimalFormatSymbols formatRp;
    int laba,totalBeli,totalJual;

    public TransaksiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaksi, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();

        ambilData();

    }

    private void ambilData() {
        refrensiList = refrensiDAO.getAllProses();
        RecyclerView recyclerView = view.findViewById(R.id.recTransaksi);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        TransaksiAdapter adapter = new TransaksiAdapter(context,refrensiList);
        recyclerView.setAdapter(adapter);


    }

    private void init() {

        refrensiDAO = Room.databaseBuilder(context, RefrensiDB.class,"refrensi")
                .allowMainThreadQueries()
                .build()
                .getRefrensiDAO();

        kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


}

