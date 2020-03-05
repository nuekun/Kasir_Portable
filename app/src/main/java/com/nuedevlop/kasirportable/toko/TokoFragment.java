package com.nuedevlop.kasirportable.toko;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.nuedevlop.kasirportable.toko.pembelian.PembelianFragment;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.toko.penjualan.PenjualanFragment;
import com.nuedevlop.kasirportable.toko.pengembalian.PengembalianFragment;


public class TokoFragment extends Fragment {

    private View view;
    private FrameLayout frameToko;
    TabLayout tabToko;
    Context context;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView txtTittle;


    public TokoFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_toko, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        txtTittle= getActivity().findViewById(R.id.txtMainTittle);
        txtTittle.setText("Penjualan");


        frameToko = view.findViewById(R.id.frameToko);
        tabToko = view.findViewById(R.id.tabToko);


        fragment = new PenjualanFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameToko, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        int tabIconColor = ContextCompat.getColor(context, R.color.tabSelectedIconColor);
        tabToko.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabToko.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new PenjualanFragment();
                        txtTittle.setText("Penjualan");
                        break;
                    case 1:
                        fragment = new PengembalianFragment();
                        txtTittle.setText("Pengembalian");
                        break;
                    case 2:
                        fragment = new PembelianFragment();
                        txtTittle.setText("Pembelian");
                        break;
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameToko, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int tabIconColor = ContextCompat.getColor(context, R.color.tabUnselectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

}

