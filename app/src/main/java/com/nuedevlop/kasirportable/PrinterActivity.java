package com.nuedevlop.kasirportable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.util.ArrayList;

public class PrinterActivity extends AppCompatActivity {
    private Printing printing = null;
    PrintingCallback printingCallback=null;
    TextView txtStatus;
    Button btnHub,btnDis,btnTes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        init();

        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
        initViews();
        initListeners();


        btnTes.setOnClickListener(v->{
            if (!Printooth.INSTANCE.hasPairedPrinter())
                startActivityForResult(new Intent(this, ScanningActivity.class ),ScanningActivity.SCANNING_FOR_PRINTER);
            else tesPrint();
        });

        btnHub.setOnClickListener(v->{
            startActivityForResult(new Intent(this, ScanningActivity.class ),ScanningActivity.SCANNING_FOR_PRINTER);
            initViews();
        });

        btnDis.setOnClickListener(v->{
            Printooth.INSTANCE.removeCurrentPrinter();
        });


    }

    private void tesPrint() {
        Log.d("xxx", "printSomePrintable ");
        if (printing!=null){
            ArrayList<Printable> al = new ArrayList<>();
            al.add( (new TextPrintable.Builder())
                    .setText("Hello World")
                    .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                    .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                    .setNewLinesAfter(1)
                    .build());

            printing.print(al);
        }
    }

    private void init() {
        btnDis = findViewById(R.id.btnPrinterPutuskan);
        btnHub = findViewById(R.id.btnPrinterHubungkan);
        btnTes = findViewById(R.id.btnPrinterTes);
        txtStatus = findViewById(R.id.txtPrinterStatus);
    }

    private void initViews() {
        if (Printooth.INSTANCE.getPairedPrinter()!=null){
            txtStatus.setText((Printooth.INSTANCE.hasPairedPrinter())?("Status : "+ Printooth.INSTANCE.getPairedPrinter().getName()):"terhubung printer");
        } else {
            txtStatus.setText("status : printer tidak terhubung");
        }
    }

    private void initListeners() {
        if (printing!=null && printingCallback==null) {
            Log.d("xxx", "initListeners ");
            printingCallback = new PrintingCallback() {

                public void connectingWithPrinter() {
                    Toast.makeText(getApplicationContext(), "Connecting with printer", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "Connecting");
                }
                public void printingOrderSentSuccessfully() {
                    Toast.makeText(getApplicationContext(), "printingOrderSentSuccessfully", Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "printingOrderSentSuccessfully");
                }
                public void connectionFailed(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "connectionFailed :"+error, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "connectionFailed : "+error);
                }
                public void onError(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "onError :"+error, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "onError : "+error);
                }
                public void onMessage(@NonNull String message) {
                    Toast.makeText(getApplicationContext(), "onMessage :" +message, Toast.LENGTH_SHORT).show();
                    Log.d("xxx", "onMessage : "+message);
                }
            };

            Printooth.INSTANCE.printer().setPrintingCallback(printingCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("xxx", "onActivityResult "+requestCode);

        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK) {
            initListeners();
        }
        initViews();
    }

}
