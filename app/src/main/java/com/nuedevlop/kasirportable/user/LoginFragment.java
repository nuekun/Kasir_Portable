package com.nuedevlop.kasirportable.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nuedevlop.kasirportable.MainActivity;
import com.nuedevlop.kasirportable.R;
import com.nuedevlop.kasirportable.utils.PasswordStrength;
import com.nuedevlop.kasirportable.utils.Utils;

public class LoginFragment extends Fragment {

    private View view;
    private Context context;
    EditText txtEmail,txtPassword;
    TextView txtLupaPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;


    public LoginFragment() {

        // Required empty public constructor
    }



    private void init() {

        mAuth = FirebaseAuth.getInstance();
        btnLogin = view.findViewById(R.id.btnLoginLogin);
        txtEmail = view.findViewById(R.id.txtLoginEmail);
        txtLupaPassword = view.findViewById(R.id.txtLoginLupaPassword);
        txtPassword = view.findViewById(R.id.txtLoginPassowrd);


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

        txtLupaPassword.setOnClickListener(v->{
            String emailAddress = txtEmail.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Pesan terkirim , Silahkan Cek Email Anda!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Email Tidak di temukan !", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "gagal mengirim reset password", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnLogin.setOnClickListener(v->{
            String email,password;
            email=txtEmail.getText().toString();
            password=txtPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

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

