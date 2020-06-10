package com.nuedevlop.kasirportable.user;

import android.content.Context;
import android.graphics.Color;
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


public class RegisterFragment extends Fragment {

    private View view;
    private Context context;
    EditText txtPassword;
    Utils utils;



    public RegisterFragment() {

        // Required empty public constructor
    }



    private void init() {
    txtPassword = view.findViewById(R.id.txtRegisterPassword);
    utils = new Utils();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (utils.ValidationForPassword(txtPassword.getText().toString())){
                    PasswordStrength str = PasswordStrength.calculateStrength(txtPassword.getText().toString());
                    Toast.makeText(context, str.getText(context), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

}


