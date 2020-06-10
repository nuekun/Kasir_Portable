package com.nuedevlop.kasirportable.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.utils.PasswordStrength;
import com.nuedevlop.kasirportable.utils.Utils;

public class LoginFragment extends Fragment {

    private View view;
    private Context context;

    Utils utils;



    public LoginFragment() {

        // Required empty public constructor
    }



    private void init() {

        utils = new Utils();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();

        //PasswordStrength str = PasswordStrength.calculateStrength(txtPassword.getText().toString());



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

}

