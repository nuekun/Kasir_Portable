package com.nuedevlop.kasirportable.toko.pembelian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nuedevlop.kasirportable.ProdukActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.ProdukAdapter;
import com.nuedevlop.kasirportable.adapter.ProsesAdapter;
import com.nuedevlop.kasirportable.utils.database.Produk;
import com.nuedevlop.kasirportable.utils.database.Proses;
import com.nuedevlop.kasirportable.utils.database.ProsesDAO;
import com.nuedevlop.kasirportable.utils.database.ProsesDB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;


public class PembelianFragment extends Fragment {

    private View view;
    private Context context;
    private Button btnProses;
    private ImageButton btnTambah;
    private final int RecKodeProduk = 210;
    private ProsesDAO prosesDAO;
    private String rpTotal;
    private List<Proses> proses;
    private DecimalFormat kursIndonesia;
    private DecimalFormatSymbols formatRp;
    private Spinner spinner;

    public PembelianFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pembelian, container, false);

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

        btnProses.setOnClickListener(v-> {
            prosesDAO.deleteALL();
            ambilProses();
        });

        ambilProses();

    }

    private void init() {
        btnProses = view.findViewById(R.id.btnPembelianProses);
        btnTambah = view.findViewById(R.id.btnPembelianTambah);
        spinner = view.findViewById(R.id.spinPembelian);

        prosesDAO = Room.databaseBuilder(context, ProsesDB.class,"pembelian")
                .allowMainThreadQueries()
                .build()
                .getProsesDAO();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RecKodeProduk && resultCode == ProdukAdapter.KodeProduk) {

            Produk produk = new Produk();
            produk = data.getParcelableExtra(ProdukAdapter.ExtraProduk);
            Proses proses = new Proses();

            Button btnBatal,btnSimpan;
            TextInputEditText txtJumlah;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.dialog_proses_tambah, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();

            txtJumlah = view.findViewById(R.id.txtDialogProsesJumlah);
            btnBatal = view.findViewById(R.id.btnDialogProsesBatal);
            btnSimpan = view.findViewById(R.id.btnDialogProsesSimpan);

            btnBatal.setOnClickListener(v -> dialog.dismiss());
            Produk finalProduk = produk;
            btnSimpan.setOnClickListener(v -> {

                if (txtJumlah.getText().length()!=0){
                    if (Integer.valueOf(txtJumlah.getText().toString())>0){
                        proses.setJumlah(Integer.parseInt(txtJumlah.getText().toString()));
                        proses.setNama(finalProduk.getNama());
                        proses.setIdProduk(finalProduk.getIdProduk());
                        proses.setJenis(finalProduk.getJenis());
                        proses.setHargaBeli(finalProduk.getHargaBeli());
                        proses.setHargaJual(finalProduk.getHargaJual());
                        proses.setDetail(spinner.getSelectedItem().toString());
                        if (prosesDAO.getProsesCountbyNama(finalProduk.getNama())>0){
                            Toast.makeText(context, "Produk sudah ada !", Toast.LENGTH_SHORT).show();
                        } else {
                            prosesDAO.insert(proses);
                        }

                    }else {
                        Toast.makeText(context, "jumlah tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Masukkan Jumlah!", Toast.LENGTH_SHORT).show();
                }

                ambilProses();
                dialog.dismiss();

            });

            dialog.show();
        }

    }

    private void ambilProses() {

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameToko);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        proses = prosesDAO.getAllProses();
        RecyclerView recyclerView = view.findViewById(R.id.recPembelian);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ProsesAdapter prosesAdapter = new ProsesAdapter("pembelian", proses,context,prosesDAO,currentFragment,fragmentTransaction);
        recyclerView.setAdapter(prosesAdapter);

        int total = 0;

        for(int i = 0 ; i<proses.size();i++){
            total = total + (proses.get(i).getHargaJual()*proses.get(i).getJumlah());
        }

        rpTotal = kursIndonesia.format(total);

    }

}