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

public class RegisterActivity extends AppCompatActivity {

    private TextView tvSendToLogin;
    private EditText edtEmailReg, edtPassReg, edtConfPassReg;
    private ProgressBar progressBar;
    private Button btnRegist;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        tvSendToLogin = findViewById(R.id.tvSendLoginActivity);
        edtEmailReg = findViewById(R.id.edtEmailRegist);
        edtPassReg = findViewById(R.id.edtPasswordRegist);
        edtPassReg.setTransformationMethod(new PasswordTransformationMethod());
        edtConfPassReg = findViewById(R.id.edtConfPasswordRegist);
        edtConfPassReg.setTransformationMethod(new PasswordTransformationMethod());
        btnRegist = findViewById(R.id.btnRegist);
        progressBar = findViewById(R.id.progressRegist);

        // perintah send to login activity
        tvSendToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        // perintah button register
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String regEmail = edtEmailReg.getText().toString();
                String regPass = edtPassReg.getText().toString();
                String regConfPass = edtConfPassReg.getText().toString();

                // perintah cek email dan password
                if (!TextUtils.isEmpty(regEmail) && !TextUtils.isEmpty(regPass) & !TextUtils.isEmpty(regConfPass)) {

                    if (regPass.equals(regConfPass)) {

                        btnRegist.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        // perintah buat akun baru
                        mAuth.createUserWithEmailAndPassword(regEmail, regPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    doSendSetupProfile();

                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Snackbar.make(v, "Upss, " + errorMessage, Snackbar.LENGTH_SHORT).show();

                                }

                                btnRegist.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });

                    } else {

                        Snackbar.make(v, "Confirm password doesn't match!", Snackbar.LENGTH_SHORT).show();

                    }

                } else {

                    Snackbar.make(v, "Please fill all field!", Snackbar.LENGTH_SHORT).show();

                }

            }
        });

    }

    // perintah intent ke setup profile
    private void doSendSetupProfile() {

        Intent intent = new Intent(RegisterActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();

    }

}
