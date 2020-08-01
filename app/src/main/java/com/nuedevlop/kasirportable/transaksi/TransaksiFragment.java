package com.nuedevlop.kasirportable.transaksi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.adapter.TransaksiAdapter;
import com.nuedevlop.kasirportable.utils.database.refrensi.Refrensi;
import com.nuedevlop.kasirportable.utils.database.refrensi.RefrensiDAO;
import com.nuedevlop.kasirportable.utils.database.refrensi.RefrensiDB;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class TransaksiFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private View view;
    private Context context;
    private RefrensiDAO refrensiDAO;
    private List<Refrensi> refrensiList;
    private DecimalFormat kursIndonesia;
    private DecimalFormatSymbols formatRp;
    ImageButton btnTanggal;
    private TextView txtValuasi;
    Spinner spinJenis;
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

        ambilData("","Semua");

        btnTanggal.setOnClickListener(v -> {
            MonthYearPickerDialog pd = new MonthYearPickerDialog();
            pd.setListener(this);
            pd.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "MonthYearPickerDialog");
        });


        spinJenis.setOnItemSelectedListener(this);




    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String bulan;
        if (month<=9) {
            bulan = "0"+month;
        } else {
            bulan = String.valueOf(month);
        }

        ambilData(year+"-"+bulan,spinJenis.getSelectedItem().toString());
    }


    public static class MonthYearPickerDialog extends DialogFragment {

        private static final int MAX_YEAR = 2099;
        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            Calendar cal = Calendar.getInstance();
            View dialog = inflater.inflate(R.layout.dialog_date, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(01);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH));

            int year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(MAX_YEAR);
            yearPicker.setValue(year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton("pilih", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                        }
                    })
                    .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MonthYearPickerDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }


    private void ambilData(String tanggal,String jenis) {
        if (jenis.equals("Semua")){
            refrensiList = refrensiDAO.getAllProses(tanggal,"");
        }else{
            refrensiList = refrensiDAO.getAllProses(tanggal,jenis);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recTransaksi);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        TransaksiAdapter adapter = new TransaksiAdapter(context,refrensiList);
        recyclerView.setAdapter(adapter);

        int jumlah = 0;

        for(Refrensi refrensi : refrensiList) {
            jumlah = jumlah + refrensi.getValuasi();
        }

        txtValuasi.setText(kursIndonesia.format(jumlah));
       // Toast.makeText(context, String.valueOf(jumlah), Toast.LENGTH_SHORT).show();

    }

    private void init() {

        refrensiDAO = Room.databaseBuilder(context, RefrensiDB.class,"refrensi")
                .allowMainThreadQueries()
                .build()
                .getRefrensiDAO();

        btnTanggal = view.findViewById(R.id.btnTransaksiTaggal);

        txtValuasi = view.findViewById(R.id.txtTransaksiValuasi);
        spinJenis = view.findViewById(R.id.spinTransaksi);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        String jenis = spinJenis.getSelectedItem().toString();
        ambilData("",jenis);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

