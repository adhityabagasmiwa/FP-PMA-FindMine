package com.planhub.findmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.planhub.findmine.Adapter.PostAdapter;
import com.planhub.findmine.Model.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView tvDetailTitle, tvDetailUsername, tvDetailDesc, tvDetailDate;
    private ImageView imgPostDetail, imgDetailUser;

    private String currentUserId;

    private List<Post> postList;
    private PostAdapter postAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("Detail");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // menampilkan tombol back

        // Init view
        tvDetailUsername = findViewById(R.id.tvNameUserDetail);
        tvDetailDate = findViewById(R.id.tvDateDetail);
        tvDetailDesc = findViewById(R.id.tvDescDetail);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        imgPostDetail = findViewById(R.id.imgPostDetail);

        imgDetailUser = findViewById(R.id.imgUserDetail);

        postList = new ArrayList<>();

        // Init firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String postTitle = getIntent().getExtras().getString("title");
        tvDetailTitle.setText(postTitle);

        String postDesc = getIntent().getExtras().getString("desc");
        tvDetailDesc.setText(postDesc);

        String datePost = timestampString(getIntent().getExtras().getLong("timestamp"));
        tvDetailDate.setText(datePost);

        String postImg = getIntent().getExtras().getString("img_url");
        Glide.with(this).load(postImg).into(imgPostDetail);

        String userName = getIntent().getExtras().getString("name");
        tvDetailUsername.setText(userName);

        String userImgProfile = getIntent().getExtras().getString("img_profile");
        Glide.with(this).load(userImgProfile).into(imgDetailUser);

        getData(userImgProfile);

        // menampilkan user
        firebaseFirestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(currentUserId);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String fullname = documentSnapshot.getString("name");
                            String kelas = documentSnapshot.getString("kelas");
                            String img_profile = documentSnapshot.getString("img_profile");

                            tvDetailUsername.setText(fullname);

                            getData(img_profile);

                        }
                    });

                } else {

                }
            }
        });
    }


    private void getData(String userImage) {

        imgDetailUser = findViewById(R.id.imgUserDetail);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(userImage).into(imgDetailUser);

    }

    private String timestampString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String dateString = DateFormat.format("MMM d, hh:mm a", calendar).toString();
        return dateString;

    }

}
