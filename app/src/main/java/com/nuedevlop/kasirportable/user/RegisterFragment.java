package com.nuedevlop.kasirportable.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nuedevlop.kasirportable.MainActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.utils.PasswordStrength;
import com.nuedevlop.kasirportable.utils.Utils;

import java.util.concurrent.Executor;


public class RegisterFragment extends Fragment {

    private View view;
    private Context context;
    Button btnRegister;
    EditText txtPassword;
    EditText txtEmail;
    private Utils utils;
    private FirebaseAuth mAuth;


    public RegisterFragment() {

        // Required empty public constructor
    }



    private void init() {
    txtPassword = view.findViewById(R.id.txtRegisterPassword);
    txtEmail = view.findViewById(R.id.txtRegisterEmail);
    btnRegister = view.findViewById(R.id.btnRegisterDaftar);
    utils = new Utils();

    mAuth = FirebaseAuth.getInstance();
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

        btnRegister.setOnClickListener(v->
        {
            String email,password;
            email = txtEmail.getText().toString();
            password = txtPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {



                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                       updateUI(user);
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }


                        }
                    });

        });

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

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

}


