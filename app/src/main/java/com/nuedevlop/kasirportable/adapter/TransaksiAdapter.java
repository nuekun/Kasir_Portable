package com.nuedevlop.kasirportable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuedevlop.kasirportable.DetailTransaksiActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.utils.database.Refrensi;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {

    private Context context;
    private List<Refrensi> refrensis;

    public TransaksiAdapter(Context context, List<Refrensi> refrensis) {
        this.context = context;
        this.refrensis = refrensis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaksi, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String tanggal,jenis,refrensi;
        int valuasi;

        tanggal = refrensis.get(position).getTanggal();
        jenis =refrensis.get(position).getJenis() ;
        refrensi=refrensis.get(position).getRefrensi();
        valuasi = refrensis.get(position).getValuasi();

        holder.txtJudul.setText(jenis);
        holder.txtKeterangan.setText(String.valueOf(valuasi));
        holder.root.setOnClickListener(v->{

            Intent intent = new Intent(context, DetailTransaksiActivity.class);
            Refrensi ref = new Refrensi();
            ref = refrensis.get(position);
            intent.putExtra(DetailTransaksiActivity.ExtraReferal, ref);
            context.startActivity(intent);
        });


    }


    @Override
    public int getItemCount() {
        return refrensis.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtJudul,txtKeterangan;
        RelativeLayout root;

        ViewHolder(@NonNull View view) {

            super(view);
            root = view.findViewById(R.id.txtListTransaksiRoot);
            txtJudul = view.findViewById(R.id.txtListTransaksiJudul);
            txtKeterangan = view.findViewById(R.id.txtListTransaksiKeterangan);
        }
    }

}