package com.nuedevlop.kasirportable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuedevlop.kasirportable.DetailTransaksiActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.utils.database.Refrensi;
import com.nuedevlop.kasirportable.utils.database.produk.Produk;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;



public class StokAdapter extends RecyclerView.Adapter<StokAdapter.ViewHolder> {

    private Context context;
    private List<Produk> produks;

    public StokAdapter(Context context, List<Produk> produks) {
        this.context = context;
        this.produks = produks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        String nama,keterangan,jenis,rpHarga,rpKulak,suplier,barcode;
        int stok , hargaBeli,hargaJual;
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        barcode = produks.get(position).getBarcode();
        suplier = produks.get(position).getSuplier();
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        hargaBeli = produks.get(position).getHargaBeli();
        hargaJual = produks.get(position).getHargaJual();
        nama = produks.get(position).getNama();
        stok  = produks.get(position).getStok();
        rpKulak = kursIndonesia.format(hargaBeli);
        rpHarga = kursIndonesia.format(hargaJual);
        jenis = produks.get(position).getJenis();
        keterangan = produks.get(position).getKeterangan();
        final int id = produks.get(position).getIdProduk();

        Produk produk = new Produk();
        produk.setStok(stok);
        produk.setHargaBeli(hargaBeli);
        produk.setHargaJual(hargaJual);
        produk.setKeterangan(keterangan);
        produk.setNama(nama);
        produk.setIdProduk(id);
        produk.setSuplier(suplier);
        produk.setBarcode(barcode);
        produk.setJenis(jenis);

        holder.lblKet.setText("");
        holder.txtKet.setText(keterangan);

        holder.txtNama.setText(nama);

        holder.txtJumlah.setText("x"+String.valueOf(stok));

    }


    @Override
    public int getItemCount() {
        return produks.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtNama, txtKet,txtJumlah,lblKet;
        LinearLayout rootJumlah;
        RelativeLayout root;
        ImageButton btnHapus;

        ViewHolder(@NonNull View view) {

            super(view);

            txtKet = view.findViewById(R.id.txtListProsesKet);
            txtNama = view.findViewById(R.id.txtListProsesNama);
            txtJumlah = view.findViewById(R.id.txtListProsesJumlah);
            rootJumlah = view.findViewById(R.id.rootListProsesJumlah);
            root = view.findViewById(R.id.rootListProses);
            btnHapus = view.findViewById(R.id.btnListProsesHapus);
            lblKet = view.findViewById(R.id.lblListProsetKet);

        }
    }

}