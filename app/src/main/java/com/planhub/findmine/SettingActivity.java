package com.planhub.findmine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingActivity extends AppCompatActivity {

    private CircleImageView imgSetupImgProfile;
    private Uri imgProfileURI = null;

    private String idUser;
    private EditText edtFullname, edtKelas;
    private TextView tvSaveSetupProfile;
    private ProgressBar progressBarSetupProfile;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        Toolbar setupToolbar = findViewById(R.id.setupBar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Edit Profile");

        // init
        imgSetupImgProfile = findViewById(R.id.imgSetupImgProfile);
        edtFullname = findViewById(R.id.edtFullname);
        edtKelas = findViewById(R.id.edtKelas);
        tvSaveSetupProfile = findViewById(R.id.tvSaveSetupProfile);
        progressBarSetupProfile = findViewById(R.id.progressBarSetupProfile);

        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBarSetupProfile.setVisibility(View.VISIBLE);
        tvSaveSetupProfile.setEnabled(false);

        // menampilkan profile sesuai iduser
        firebaseFirestore.collection("Users").document(idUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        String fullname = task.getResult().getString("name");
                        String kelas = task.getResult().getString("kelas");
                        String img_profile = task.getResult().getString("img_profile");

                        imgProfileURI = Uri.parse(img_profile);

                        edtFullname.setText(fullname);
                        edtKelas.setText(kelas);

                        RequestOptions placeholderReq = new RequestOptions();
                        placeholderReq.placeholder(R.drawable.upload_user_image);

                        Glide.with(SettingActivity.this).setDefaultRequestOptions(placeholderReq).load(img_profile).into(imgSetupImgProfile);

                    }

                } else {

                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SettingActivity.this, "Upss, firestore error " + errorMessage, Toast.LENGTH_SHORT).show();

                }

                progressBarSetupProfile.setVisibility(View.INVISIBLE);
                tvSaveSetupProfile.setEnabled(true);

            }
        });

        // perintah save button
        tvSaveSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String username = edtFullname.getText().toString();
                final String kelas = edtKelas.getText().toString();

                if (!TextUtils.isEmpty(username) && imgProfileURI != null) {

                    idUser = mAuth.getCurrentUser().getUid();
                    progressBarSetupProfile.setVisibility(View.VISIBLE);

                    final StorageReference imgPath = storageReference.child("image_profile").child(idUser + ".jpg");
                    imgPath.putFile(imgProfileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    // menyimpan url ke firebase firestore
                                    String URIdownload = uri.toString();

                                    // menyimpan data ke firebase firestore menggunakan HashMap method
                                    HashMap<String, Object> userMap = new HashMap<>();
                                    userMap.put("name", username);
                                    userMap.put("kelas", kelas);
                                    userMap.put("img_profile", URIdownload);

                                    firebaseFirestore.collection("Users").document(idUser).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            } else {

                                                String errorMessage = task.getException().getMessage();
                                                Snackbar.make(v, "Upss, firestore error " + errorMessage, Snackbar.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    String errorMessage = e.getMessage();
                                    Snackbar.make(v, "Upss, " + errorMessage, Snackbar.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });

                }

            }
        });


        // perintah add photo
        imgSetupImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // perintah untuk mengakses permission penyimpanan pada hp
                if (Build.VERSION.SDK_INT >= 21) {

                    if (ContextCompat.checkSelfPermission(SettingActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(SettingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        imgPicker();

                    }

                } else {

                    imgPicker();

                }
            }

        });
    }

    // setup crop image
    private void imgPicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SettingActivity.this);

    }

    // perintah crop image saat upload photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imgProfileURI = result.getUri();
                imgSetupImgProfile.setImageURI(imgProfileURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
