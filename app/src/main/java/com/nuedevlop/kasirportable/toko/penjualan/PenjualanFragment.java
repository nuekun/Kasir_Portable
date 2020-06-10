package com.nuedevlop.kasirportable.toko.penjualan;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.nuedevlop.kasirportable.ProdukActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.ProdukAdapter;
import com.nuedevlop.kasirportable.adapter.ProsesAdapter;
import com.nuedevlop.kasirportable.utils.Utils;
import com.nuedevlop.kasirportable.utils.database.Refrensi;
import com.nuedevlop.kasirportable.utils.database.RefrensiDAO;
import com.nuedevlop.kasirportable.utils.database.RefrensiDB;
import com.nuedevlop.kasirportable.utils.database.Transaksi;
import com.nuedevlop.kasirportable.utils.database.TransaksiDAO;
import com.nuedevlop.kasirportable.utils.database.TransaksiDB;
import com.nuedevlop.kasirportable.utils.database.produk.Produk;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDB;
import com.nuedevlop.kasirportable.utils.database.proses.Proses;
import com.nuedevlop.kasirportable.utils.database.proses.ProsesDAO;
import com.nuedevlop.kasirportable.utils.database.proses.ProsesDB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.nuedevlop.kasirportable.utils.Utils.formate_refrensi;
import static com.nuedevlop.kasirportable.utils.Utils.formate_year_month_day_hour_minute_second;


public class PenjualanFragment extends Fragment {

    private View view;
    private Context context;
    Button btnProses;
    ImageButton btnTambah;
    private final int RecKodeProduk = 210;
    private ProsesDAO prosesDAO;
    private TransaksiDAO transaksiDAO;
    private TextView txtTotal;
    private List<Proses> proses;
    private DecimalFormat kursIndonesia;
    private DecimalFormatSymbols formatRp;
    private Printing printing = null;
    private PrintingCallback printingCallback=null;
    private String refrensi,tanggal;
    private Date date = new Date();
    private UUID uuid = UUID.randomUUID();
    RefrensiDAO refrensiDAO ;
    private ProdukDAO produkDAO;

    int laba,totalBeli,totalJual;

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


        btnProses.setOnClickListener(v-> {

            Button btnBatal,btnSimpan,btncetak;

            tanggal = Utils.get_string_from_date_object(date,formate_year_month_day_hour_minute_second);
            refrensi = uuid.toString()+Utils.get_string_from_date_object(date,formate_refrensi);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @SuppressLint("InflateParams") View v2 = LayoutInflater.from(context).inflate(R.layout.dialog_cetak_struk, null);
            builder.setView(v2);
            AlertDialog dialog = builder.create();

            btnBatal = v2.findViewById(R.id.btnDialogCetakBatal);
            btnSimpan = v2.findViewById(R.id.btnDialogCetakTidak);
            btncetak = v2.findViewById(R.id.btnDialogCetakYa);

            btnBatal.setOnClickListener(v3->dialog.dismiss());
            btnSimpan.setOnClickListener(v3->{
                simpanTransaksi(tanggal,refrensi);
                dialog.dismiss();

            });
            btncetak.setOnClickListener(v3->{
                cetakStrukk();
                simpanTransaksi(tanggal,refrensi);
                dialog.dismiss();
            });

            if (proses.size()>0){
                dialog.show();
            }

        });

        ambilProses();

    }

    private void simpanTransaksi(String tanggal,String refrensi) {

        Refrensi ref = new Refrensi();

        for (Proses proses : proses) {
            Transaksi transaksi = new Transaksi();
                transaksi.setDetail(proses.getDetail());
                transaksi.setHargaBeli(proses.getHargaBeli());
                transaksi.setHargaJual(proses.getHargaJual());
                transaksi.setIdProduk(proses.getIdProduk());
                transaksi.setJumlah(proses.getJumlah());
                transaksi.setNama(proses.getNama());
                transaksi.setJenis(proses.getJenis());
                transaksi.setTipe("penjualan");
                transaksi.setRefrensi(refrensi);
                transaksi.setTanggal(tanggal);
            transaksiDAO.insert(transaksi);

            List<Produk> produkList = produkDAO.getProdukByID(proses.getIdProduk());

            produkDAO.updateStok(produkList.get(0).getStok() - proses.getJumlah(),proses.getIdProduk());

        }

        ref.setRefrensi(refrensi);
        ref.setJenis("penjualan");
        ref.setTanggal(tanggal);
        ref.setValuasi(laba);

        refrensiDAO.insert(ref);

        prosesDAO.deleteALL();
        ambilProses();
    }

    private void cetakStrukk(){

        //kursIndonesia.format(total);

        if (printing!=null){
            ArrayList<Printable> al = new ArrayList<>();

//            al.add((new TextPrintable.Builder())
//                    .setText("Nama Toko")
//                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
//                    .setNewLinesAfter(1)
//                    .build());
//            al.add((new TextPrintable.Builder())
//                    .setText("Alamat Toko dan no HP")
//                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
//                    .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
//                    .setNewLinesAfter(1)
//                    .build());
//

            int totalItem = 0 ;
            int tagihan=0;

            for(int position = 0 ; position<proses.size();position++){
                String nama = proses.get(position).getNama();
                String jenis = proses.get(position).getJenis();
                int jumlah = proses.get(position).getJumlah();
                int hargaJual = proses.get(position).getHargaJual();
                String harga = kursIndonesia.format(jumlah*hargaJual);
                al.add((new TextPrintable.Builder())
                        .setText("("+jenis+")"+nama+" x "+jumlah)
                        .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                        .setNewLinesAfter(1)
                        .build());

                al.add((new TextPrintable.Builder())
                        .setText(harga)
                        .setAlignment(DefaultPrinter.Companion.getALIGNMENT_RIGHT())
                        .setNewLinesAfter(1)
                        .build());

                totalItem = totalItem + jumlah;
                tagihan = tagihan +(jumlah*hargaJual);

            }



            al.add((new TextPrintable.Builder())
                    .setText("Total item "+totalItem)
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setNewLinesAfter(1)
                    .build());

            al.add((new TextPrintable.Builder())
                    .setText("Tagihan "+"("+kursIndonesia.format(tagihan)+")")
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_RIGHT())
                    .setNewLinesAfter(1)
                    .build());



            printing.print(al);
        }else {
            Toast.makeText(context, "Printer Tidak Terhubung!", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {
        btnProses = view.findViewById(R.id.btnPenjualanProses);
        btnTambah = view.findViewById(R.id.btnPenjualanTambah);
        txtTotal = view.findViewById(R.id.txtPenjualanTotal);

        produkDAO = Room.databaseBuilder(context, ProdukDB.class,"Produk")
                .allowMainThreadQueries()
                .build()
                .getProdukDAO();

        refrensiDAO = Room.databaseBuilder(context, RefrensiDB.class,"refrensi")
                .allowMainThreadQueries()
                .build()
                .getRefrensiDAO();

        transaksiDAO = Room.databaseBuilder(context, TransaksiDB.class,"transaksi")
                .allowMainThreadQueries()
                .build()
                .getTransaksiDAO();

        prosesDAO = Room.databaseBuilder(context, ProsesDB.class,"penjulan")
                .allowMainThreadQueries()
                .build()
                .getProsesDAO();

        kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();

        if (printing!=null && printingCallback==null) {
            Log.d("xxx", "initListeners ");
            printingCallback = new PrintingCallback() {

                public void connectingWithPrinter() {
                    Toast.makeText(context, "Connecting with printer", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "Connecting");
                }
                public void printingOrderSentSuccessfully() {
                    Toast.makeText(context, "printingOrderSentSuccessfully", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "printingOrderSentSuccessfully");
                }
                public void connectionFailed(@NonNull String error) {
                    Toast.makeText(context, "connectionFailed :"+error, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "connectionFailed : "+error);
                }
                public void onError(@NonNull String error) {
                    Toast.makeText(context, "onError :"+error, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "onError : "+error);
                }
                public void onMessage(@NonNull String message) {
                    Toast.makeText(context, "onMessage :" +message, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "onMessage : "+message);
                }
            };

            Printooth.INSTANCE.printer().setPrintingCallback(printingCallback);
        }


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

                        List<Produk> produkList = produkDAO.getProdukByID(finalProduk.getIdProduk());
                        int stok = produkList.get(0).getStok();

                        if(stok<Integer.parseInt(txtJumlah.getText().toString())){
                            Toast.makeText(context, "stok tidak cukup", Toast.LENGTH_SHORT).show();
                        }else{
                            proses.setJumlah(Integer.parseInt(txtJumlah.getText().toString()));
                            proses.setNama(finalProduk.getNama());
                            proses.setIdProduk(finalProduk.getIdProduk());
                            proses.setJenis(finalProduk.getJenis());
                            proses.setHargaBeli(finalProduk.getHargaBeli());
                            proses.setHargaJual(finalProduk.getHargaJual());
                            proses.setDetail("Penjualan");
                            if (prosesDAO.getProsesCountbyNama(finalProduk.getNama())>0){
                                Toast.makeText(context, "Produk sudah ada !", Toast.LENGTH_SHORT).show();
                            } else {
                                prosesDAO.insert(proses);
                            }
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

        Fragment currentFragment = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.frameToko);
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();


        proses = prosesDAO.getAllProses();
        RecyclerView recyclerView = view.findViewById(R.id.recPenjualan);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ProsesAdapter prosesAdapter = new ProsesAdapter("penjualan",proses,context,prosesDAO,currentFragment,fragmentTransaction);
        recyclerView.setAdapter(prosesAdapter);

        totalJual = 0;
        totalBeli= 0;
        laba = 0;

        for(int i = 0 ; i<proses.size();i++){
            totalJual = totalJual + (proses.get(i).getHargaJual()*proses.get(i).getJumlah());
            totalBeli = totalBeli + (proses.get(i).getHargaBeli()*proses.get(i).getJumlah());
        }
        laba = totalJual-totalBeli;
        txtTotal.setText(kursIndonesia.format(totalBeli));



    }




}


