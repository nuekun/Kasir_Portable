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
import com.nuedevlop.kasirportable.utils.database.Transaksi;

import java.util.List;

public class DetailTransaksiAdapter extends RecyclerView.Adapter<DetailTransaksiAdapter.ViewHolder> {

    private Context context;
    private List<Transaksi> transaksis;

    public DetailTransaksiAdapter(Context context, List<Transaksi> transaksis) {
        this.context = context;
        this.transaksis = transaksis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String produk = transaksis.get(position).getNama();
        String jumlah = String.valueOf(transaksis.get(position).getJumlah());
        String harga = String.valueOf(transaksis.get(position).getHargaJual());
        String tipe = transaksis.get(position).getTipe();
        holder.txtNama.setText(produk);
        holder.txtJumlah.setText("x"+jumlah);

        if (tipe.equals("penjualan")){
            holder.txtKet.setText("RP." + harga);
        }else {
            holder.lblKet.setText("");
            holder.txtKet.setText(tipe);
        }


    }


    @Override
    public int getItemCount() {
        return transaksis.size();
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