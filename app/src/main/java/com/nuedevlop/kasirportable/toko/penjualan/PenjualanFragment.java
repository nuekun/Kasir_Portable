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
import com.nuedevlop.kasirportable.utils.database.Produk;
import com.nuedevlop.kasirportable.utils.database.Proses;
import com.nuedevlop.kasirportable.utils.database.ProsesDAO;
import com.nuedevlop.kasirportable.utils.database.ProsesDB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;


public class PenjualanFragment extends Fragment {

    private View view;
    private Context context;
    Button btnProses;
    ImageButton btnTambah;
    private final int RecKodeProduk = 210;
    ProsesDAO prosesDAO;
    TextView txtTotal;
    List<Proses> proses;
    DecimalFormat kursIndonesia;
    DecimalFormatSymbols formatRp;
    private Printing printing = null;
    PrintingCallback printingCallback=null;

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
            cetakStrukk();
            prosesDAO.deleteALL();
            ambilProses();
        });

        ambilProses();



    }

    private void cetakStrukk(){

        //kursIndonesia.format(total);

        if (printing!=null){
            ArrayList<Printable> al = new ArrayList<>();

            al.add((new TextPrintable.Builder())
                    .setText("Nama Toko")
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setNewLinesAfter(1)
                    .build());
            al.add((new TextPrintable.Builder())
                    .setText("Alamat Toko dan no HP")
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                    .setNewLinesAfter(1)
                    .build());


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
                        proses.setJumlah(Integer.parseInt(txtJumlah.getText().toString()));
                        proses.setNama(finalProduk.getNama());
                        proses.setIdProduk(finalProduk.getIdProduk());
                        proses.setJenis(finalProduk.getJenis());
                        proses.setHargaBeli(finalProduk.getHargaBeli());
                        proses.setHargaJual(finalProduk.getHargaJual());
                        proses.setDetail("");
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
        RecyclerView recyclerView = view.findViewById(R.id.recPenjualan);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ProsesAdapter prosesAdapter = new ProsesAdapter("penjualan",proses,context,prosesDAO,currentFragment,fragmentTransaction);
        recyclerView.setAdapter(prosesAdapter);

        int total = 0;

        for(int i = 0 ; i<proses.size();i++){
            total = total + (proses.get(i).getHargaJual()*proses.get(i).getJumlah());
        }

        txtTotal.setText(kursIndonesia.format(total));



    }




}


