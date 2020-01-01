package com.planhub.findmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPass;
    private TextView tvRegist;
    private Button btnLogin;
    private ProgressBar progressBarLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        tvRegist = findViewById(R.id.tvRegist);
        edtPass = findViewById(R.id.edtPassword);
        edtPass.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin = findViewById(R.id.btnLogin);
        progressBarLogin = findViewById(R.id.progressLogin);

        // perintah button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String loginEmail = edtEmail.getText().toString();
                String loginPass = edtPass.getText().toString();

                //Fungsi login
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) {

                    //Membuat tampilan progressbar
                    progressBarLogin.setVisibility(View.VISIBLE);

                    //Metode login email dan password di Firebase
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //Pesan saat login
                            if (task.isSuccessful()) {

                                doSendMainActivity();

                            } else {

                                String errorMessage = task.getException().getMessage();
                                Snackbar.make(v, "Upss, " + errorMessage , Snackbar.LENGTH_SHORT).show();
                                /*Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();*/

                            }

                            progressBarLogin.setVisibility(View.INVISIBLE);

                        }

                    });

                }
            }
        });

        // Intent ke register
        tvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //User login
            doSendMainActivity();

        }
    }


    //Pindah activity
    private void doSendMainActivity() {
        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginIntent);
        Toast.makeText(LoginActivity.this, "Behasil Login", Toast.LENGTH_LONG).show();
        finish();
    }
}
