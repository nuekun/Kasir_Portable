package com.nuedevlop.kasirportable;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.nuedevlop.kasirportable.adapter.DetailTransaksiAdapter;
import com.nuedevlop.kasirportable.utils.database.refrensi.Refrensi;
import com.nuedevlop.kasirportable.utils.database.transaksi.Transaksi;
import com.nuedevlop.kasirportable.utils.database.transaksi.TransaksiDAO;
import com.nuedevlop.kasirportable.utils.database.transaksi.TransaksiDB;

import java.util.List;


public class DetailTransaksiActivity extends AppCompatActivity {

    String refrensi;
    TextView txtIdRefrensi;
    TransaksiDAO transaksiDAO;
    List<Transaksi> transaksiList;

    public static final String ExtraReferal = "referensi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        init();

        Refrensi refrensis = getIntent().getParcelableExtra(ExtraReferal);

        refrensi = refrensis.getRefrensi();

        txtIdRefrensi.setText(refrensi);

        ambilDetailTransaksi(refrensi);

    }

    private void ambilDetailTransaksi(String refrensi) {

        transaksiList = transaksiDAO.getDetailByRefrensi(refrensi);
        RecyclerView recyclerView = findViewById(R.id.recDetailTransaksi);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        DetailTransaksiAdapter adapter = new DetailTransaksiAdapter(this,transaksiList);
        recyclerView.setAdapter(adapter);

    }

    private void init() {

        transaksiDAO = Room.databaseBuilder(this, TransaksiDB.class,"transaksi")
                .allowMainThreadQueries()
                .build()
                .getTransaksiDAO();

        txtIdRefrensi = findViewById(R.id.txtDetailTransaksiID);
    }
}
