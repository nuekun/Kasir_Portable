package com.nuedevlop.kasirportable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.utils.database.proses.Proses;
import com.nuedevlop.kasirportable.utils.database.proses.ProsesDAO;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class ProsesAdapter extends RecyclerView.Adapter<ProsesAdapter.ViewHolder> {
    List<Proses> prosesList;
    Context context;
    ProsesDAO prosesDAO;
    Fragment currentFragment;
    FragmentTransaction fragmentTransaction;
    String type;

    public ProsesAdapter(String type, List<Proses> prosesList, Context context, ProsesDAO prosesDAO, Fragment currentFragment, FragmentTransaction fragmentTransaction){
        this.prosesList = prosesList;
        this.context = context;
        this.prosesDAO = prosesDAO;
        this.currentFragment = currentFragment;
        this.fragmentTransaction = fragmentTransaction;
        this.type = type;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nama,harga,keterangan;
        int hargaJual,jumlah,idProses;
        keterangan = prosesList.get(position).getDetail();
        nama = prosesList.get(position).getNama();
        hargaJual= prosesList.get(position).getHargaJual();
        jumlah= prosesList.get(position).getJumlah();
        idProses = prosesList.get(position).getIdProses();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        harga = kursIndonesia.format(hargaJual);

        if(type.equals("penjualan")){
            holder.lblKet.setText("Harga :");
            holder.txtKet.setText(harga);
        }else if(type.equals("pembelian")){
            holder.lblKet.setText("Jenis :");
            holder.txtKet.setText(keterangan);
        }else {
            holder.lblKet.setText("Alasan :");
            holder.txtKet.setText(keterangan);
        }



        holder.txtNama.setText(nama);

        holder.txtJumlah.setText("x"+String.valueOf(jumlah));

        holder.root.setOnLongClickListener(v->{
            if (holder.btnHapus.getVisibility()==View.GONE){
                holder.btnHapus.setVisibility(View.VISIBLE);
            } else {
                holder.btnHapus.setVisibility(View.GONE);
            }
            return false;
        });

        holder.btnHapus.setOnClickListener(v->{
            prosesDAO.deleteByID(idProses);
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();

        });

        holder.rootJumlah.setOnClickListener(v->{
            Button btnBatal,btnSimpan;
            TextInputEditText txtJumlah;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.dialog_proses_tambah, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();

            txtJumlah = view.findViewById(R.id.txtDialogProsesJumlah);
            btnBatal = view.findViewById(R.id.btnDialogProsesBatal);
            btnSimpan = view.findViewById(R.id.btnDialogProsesSimpan);

            btnBatal.setOnClickListener(v2 -> dialog.dismiss());
            btnSimpan.setOnClickListener(v2->{

                if (txtJumlah.getText().length()>0){
                    if (txtJumlah.getText().equals("0")){
                        Toast.makeText(context, "jumlah tidak boleh 0 !", Toast.LENGTH_SHORT).show();
                    }else {
                        prosesDAO.updateByID(Integer.parseInt(txtJumlah.getText().toString()), idProses);
                        dialog.dismiss();
                        fragmentTransaction.detach(currentFragment);
                        fragmentTransaction.attach(currentFragment);
                        fragmentTransaction.commit();
                    }
                } else {
                    Toast.makeText(context, "Masukkan Jumlah Produk !", Toast.LENGTH_SHORT).show();
                }


            });


            dialog.show();
        });




    }

    @Override
    public int getItemCount() {
        return prosesList.size();
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
