package com.planhub.findmine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;


public class PostActivity extends AppCompatActivity {

    private Toolbar postToolbar;
    private Uri postImageURI = null;

    private ImageView imgPost, imgCloseBtn;
    private TextView tvPostBtn;
    private EditText edtPostTitle, edtPostDesc;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    private String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post);

        // Init view
        imgCloseBtn = findViewById(R.id.imgClose);
        imgPost = findViewById(R.id.imgPost);
        tvPostBtn = findViewById(R.id.tvPost);
        edtPostTitle = findViewById(R.id.edtPostTitle);
        edtPostDesc = findViewById(R.id.edtPostDesc);

        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        imgCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


        tvPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postImage();

            }
        });

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(PostActivity.this);
    }

    private void postImage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting...");
        pd.show();

        final String title = edtPostTitle.getText().toString();
        final String desc = edtPostDesc.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && postImageURI != null) {

            String randomName = FieldValue.serverTimestamp().toString();

            final StorageReference postPath = storageReference.child("image_post").child(randomName + ".jpg");
            postPath.putFile(postImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    postPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String postImageURI = uri.toString();

                            // menyimpan data ke firebase firestore menggunakan HashMap method
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("img_url", postImageURI);
                            userMap.put("title", title);
                            userMap.put("desc", desc);
                            userMap.put("id_user", idUser);
                            userMap.put("timestamp", FieldValue.serverTimestamp());

                            /*DocumentReference ref = collection("Posts").document();
                            String myId = ref.getId();*/

                            firebaseFirestore.collection("Posts").add(userMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(PostActivity.this, "Post added", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        Toast.makeText(PostActivity.this, "Post not added!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    });
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    // perintah crop image saat upload photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageURI = result.getUri();
                imgPost.setImageURI(postImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

}
