package com.nuedevlop.kasirportable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.nuedevlop.kasirportable.ProdukActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.TambahProdukActivity;
import com.nuedevlop.kasirportable.utils.database.Produk;
import com.nuedevlop.kasirportable.utils.database.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.ProdukDB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {

    public static final int KodeProduk = 210;
    public static final String ExtraProduk = "pembelian";


    private Context context;
    private List<Produk> produks;
    private ProdukDAO produkDAO;


    public ProdukAdapter(Context context, List<Produk> produks) {
        this.context = context;
        this.produks = produks;
        produkDAO = Room.databaseBuilder(context, ProdukDB.class,"Produk")
                .allowMainThreadQueries()
                .build()
                .getProdukDAO();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, null);
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

        holder.txtnama.setText(nama);
        holder.txtKeterangan.setText(keterangan);

        holder.root.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.putExtra(ExtraProduk, produk);

            ((ProdukActivity)context).setResult(KodeProduk,intent);
            ((ProdukActivity)context).finish();

        });
        holder.root.setOnLongClickListener(v -> {
            //Toast.makeText(context, keterangan, Toast.LENGTH_SHORT).show();
            if (holder.btnHapus.getVisibility() == View.VISIBLE){
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDetail.setVisibility(View.GONE);
                holder.btnHapus.setVisibility(View.GONE);
            }else {
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDetail.setVisibility(View.VISIBLE);
                holder.btnHapus.setVisibility(View.VISIBLE);
            }

            return true;
        });


        holder.btnHapus.setOnClickListener(v->{

                produkDAO.deleteByidProduk(id);
                produks.remove(position);
                this.notifyItemRemoved(position);
                this.notifyItemRangeChanged(position, produks.size());
                this.notifyDataSetChanged();
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDetail.setVisibility(View.GONE);
                holder.btnHapus.setVisibility(View.GONE);

        });

        holder.btnDetail.setOnClickListener(v->{

            TextView txtNama,txtBarcode,txtSuplier,txtJenis,txtBeli,txtJual,txtStok,txtKeterangan;
            Button btnKembali;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.detail_produk, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();

            txtNama = view.findViewById(R.id.txtDetailProdukNama);
            txtBarcode = view.findViewById(R.id.txtDetailProdukBarcode);
            txtSuplier = view.findViewById(R.id.txtDetailProdukSuplier);
            txtJenis  = view.findViewById(R.id.txtDetailProdukJenis);
            txtBeli = view.findViewById(R.id.txtDetailProdukBeli);
            txtJual = view.findViewById(R.id.txtDetailProdukJual);
            txtStok = view.findViewById(R.id.txtDetailProdukStok);
            txtKeterangan= view.findViewById(R.id.txtDetailProdukKeterangan);
            btnKembali = view.findViewById(R.id.btnDetailProdukKembali);

            txtNama.setText(nama);
            txtBarcode.setText(barcode);
            txtSuplier.setText(suplier);
            txtJenis.setText(jenis);
            txtJual.setText(rpHarga);
            txtBeli.setText(rpKulak);
            txtStok.setText(String.valueOf(stok));
            txtKeterangan.setText(keterangan);

            btnKembali.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();

        });

        holder.btnEdit.setOnClickListener(v->{

            Intent intent = new Intent(context, TambahProdukActivity.class);
            intent.putExtra(TambahProdukActivity.EXTRA_PRODUK, produk);
            context.startActivity(intent);
            ((ProdukActivity)context).finish();

        });




    }




    @Override
    public int getItemCount() {
        return produks.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtnama,txtKeterangan;
        RelativeLayout root;
        ImageButton btnHapus,btnDetail,btnEdit;

        ViewHolder(@NonNull View view) {

            super(view);
            root = view.findViewById(R.id.rootProduk);
            txtnama = view.findViewById(R.id.txtListProdukNama);
            btnDetail = view.findViewById(R.id.btnListProdukDetail);
            btnEdit = view.findViewById(R.id.btnListProdukEdit);
            btnHapus = view.findViewById(R.id.btnListProdukHapus);
            txtKeterangan = view.findViewById(R.id.txtListProdukKeterangan);
        }
    }

}
