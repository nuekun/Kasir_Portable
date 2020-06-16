package com.nuedevlop.kasirportable;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mortgage.fauxiq.pawnbroker.utils.CSVReader;
import com.nuedevlop.kasirportable.stok.StokFragment;
import com.nuedevlop.kasirportable.toko.TokoFragment;
import com.nuedevlop.kasirportable.transaksi.TransaksiFragment;
import com.nuedevlop.kasirportable.utils.CSV;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDAO;
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {
    NavigationView drawer;
    DrawerLayout drawerLayout;
    ImageButton btnDrawer;
    TextView txtTittle;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    int count = 3;
    CSV csv;
    ProdukDAO produkDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnDrawer.setOnClickListener(v-> drawerLayout.openDrawer(GravityCompat.START));

        drawer.setNavigationItemSelectedListener(drawerListener);
        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new TokoFragment()).commit();
            drawer.setCheckedItem(R.id.drawerToko);
        }
    }

    private void init() {
        drawer = findViewById(R.id.mainDrawer);
        drawerLayout = findViewById(R.id.mainDrawerLayout);
        btnDrawer = findViewById(R.id.btnDrawer);
        txtTittle = findViewById(R.id.txtMainTittle);
        mAuth = FirebaseAuth.getInstance();
        csv = new CSV();
        produkDAO = Room.databaseBuilder(MainActivity.this, ProdukDB.class,"Produk")
                .allowMainThreadQueries()
                .build()
                .getProdukDAO();
    }

    NavigationView.OnNavigationItemSelectedListener drawerListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.drawerToko:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new TokoFragment()).commit();
                    drawer.setCheckedItem(R.id.drawerToko);
                    break;
                case R.id.drawerTransaksi:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new TransaksiFragment()).commit();
                    drawer.setCheckedItem(R.id.drawerTransaksi);
                    txtTittle.setText("Transaksi");
                    break;
                case R.id.drawerStok:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new StokFragment()).commit();
                    drawer.setCheckedItem(R.id.drawerStok);
                    txtTittle.setText("Stok");
                    break;
                case R.id.drawerLogout:
                    Logout();
                    break;
                case R.id.drawerPrinter:
                    Intent intent = new Intent(MainActivity.this,PrinterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.drawerProfil:
                    boolean emailVerified = currentUser.isEmailVerified();
                    if(emailVerified==true){

                    }else{
                        Toast.makeText(MainActivity.this, "silahkan konfirmasi email terebih dahulu, sikahkan cek email masuk.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.drawerbackup:
                   // backupProduk();

                    permission();
                    csv.exportProduk(produkDAO,MainActivity.this);

                    File file = new File(getFilesDir(),"/kasir/produk.cvs");
                    Uri path = FileProvider.getUriForFile(MainActivity.this,"com.nuedevlop.kasirportable.fileprovider",file);
                    Intent upload = new Intent(Intent.ACTION_SEND);
                    upload.putExtra(Intent.EXTRA_SUBJECT,"produk");
                    upload.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    upload.putExtra(Intent.EXTRA_STREAM,path);
                    startActivity(Intent.createChooser(upload,"simpan ke "));

                    break;
                case R.id.drawerLoad:
                   // loadProduk();
                    permission();
                    CSVReader csvReader = null;

                    try {

                        csvReader = new CSVReader(new FileReader(Environment.getExternalStorageDirectory() + "/kasir/produk.csv"));
                        csv.importProduk(csvReader,produkDAO,MainActivity.this);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    };


//    private void loadProduk() {
//        permission();
//        try {
//        CSVReader csvReader = new CSVReader(new FileReader(Environment.getExternalStorageDirectory() + "/kasir portable produk.csv"));
//
//        String[] nextLine;
//        int count =0;
//        StringBuilder columns = new StringBuilder();
//        StringBuilder value = new StringBuilder();
//
//            ProdukDAO produkDAO = Room.databaseBuilder(MainActivity.this, ProdukDB.class,"Produk")
//                    .allowMainThreadQueries()
//                    .build()
//                    .getProdukDAO();
//
//            while ((nextLine = csvReader.readNext()) != null) {
//                for (int i = 0; i < nextLine.length -1 ; i++) {
//                    if (count == 0) {
//                        if (i == nextLine.length - 2){
//                            columns.append(nextLine[i]);
//                            count=1;}
//                        else
//                            columns.append(nextLine[i]).append(",");
//                    } else {
//
//                        if (i == nextLine.length - 2){
//                            value.append("'").append(nextLine[i]).append("'");
//                            count = 2;
//                    }
//                        else
//                            value.append("'").append(nextLine[i]).append("',");
//                    }
//
//                    if (count ==2){
////                        SimpleSQLiteQuery query = new SimpleSQLiteQuery("Insert INTO " + "produk" + " values(" + value + ")",
////                                new Object[]{});
////                        produkDAO.insertDataRawFormat(query);
////                        value = new StringBuilder();
//                        Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//
//
//
//
//
//
//        } catch (FileNotFoundException e) {
//            Toast.makeText(MainActivity.this, "file tidak di temukan", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }


//    private void backupProduk() {
//        permission();
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//
//        File file = new File(exportDir, "kasir portable produk.csv");
//        try {
//            file.createNewFile();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//            ProdukDAO produkDAO = Room.databaseBuilder(MainActivity.this, ProdukDB.class,"Produk")
//                    .allowMainThreadQueries()
//                    .build()
//                    .getProdukDAO();
//
//            Cursor curCSV = produkDAO.getALL();
//            csvWrite.writeNext(curCSV.getColumnNames());
//            while (curCSV.moveToNext()) {
//                //Which column you want to exprort
//                String arrStr[] = new String[curCSV.getColumnCount()];
//                for (int i = 0; i < curCSV.getColumnCount(); i++)
//                    arrStr[i] = curCSV.getString(i);
//                csvWrite.writeNext(arrStr);
//            }
//            csvWrite.close();
//            curCSV.close();
//            Toast.makeText(MainActivity.this, "Exported", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception sqlEx) {
//            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
//        }
//
//    }

    private void permission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                99);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 99) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                permission();
            }
        }
    }
    private void Logout() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this,EmailPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI();
    }

    private void updateUI() {

        if(currentUser == null){
            Intent intent = new Intent(this,EmailPasswordActivity.class);
            startActivity(intent);
            finish();
        }else{
//            boolean emailVerified = currentUser.isEmailVerified();
//            String name = currentUser.getDisplayName();
//            String email = currentUser.getEmail();
//            Uri photoUrl = currentUser.getPhotoUrl();
//
//            if (emailVerified==false){
//                Toast.makeText(this, email + " status verifikasi = "+emailVerified, Toast.LENGTH_SHORT).show();
//            }else if (emailVerified==true){
//                Toast.makeText(this, email + " status verifikasi = "+emailVerified, Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            if (count!=0){
                count--;
                Toast.makeText(MainActivity.this, "tekan kembali x"+count +" lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
            } if (count==0){
                finish();
            }
        }
    }
}
