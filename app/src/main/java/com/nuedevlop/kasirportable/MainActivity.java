package com.nuedevlop.kasirportable;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.nuedevlop.kasirportable.stok.StokFragment;
import com.nuedevlop.kasirportable.toko.TokoFragment;
import com.nuedevlop.kasirportable.transaksi.TransaksiFragment;

public class MainActivity extends AppCompatActivity {
    NavigationView drawer;
    DrawerLayout drawerLayout;
    ImageButton btnDrawer;
    TextView txtTittle;

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
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            finish();
        }
    }
}
