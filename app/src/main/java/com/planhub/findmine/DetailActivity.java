package com.planhub.findmine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView tvDetailTitle, tvDetailUsername, tvDetailDesc, tvDetailDate, postId;
    private ImageView imgPostDetail, imgDetailUser;

    FirebaseAuth mAuth;
    FirebaseUser currentUserId;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Init view
        tvDetailUsername = findViewById(R.id.tvNameUserDetail);
        tvDetailDate = findViewById(R.id.tvDateDetail);
        tvDetailDesc = findViewById(R.id.tvDescDetail);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        imgPostDetail = findViewById(R.id.imgPostDetail);
        imgDetailUser = findViewById(R.id.imgUserDetail);

        // Init firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        String postTitle = getIntent().getExtras().getString("title");
        tvDetailTitle.setText(postTitle);

        String postDesc = getIntent().getExtras().getString("desc");
        tvDetailDesc.setText(postDesc);

        String datePost = timestampString(getIntent().getExtras().getLong("timestamp"));
        tvDetailDate.setText(datePost);

        String postImg = getIntent().getExtras().getString("img_url");
        Glide.with(this).load(postImg).into(imgPostDetail);

    }

    public void setUserData(String name, String img_profile) {

        tvDetailUsername = findViewById(R.id.tvNameUserDetail);
        imgDetailUser = findViewById(R.id.imgUserDetail);

        tvDetailUsername.setText(name);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(img_profile).into(imgDetailUser);

    }

    private String timestampString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String dateString = DateFormat.format("MMM d, hh:mm a", calendar).toString();
        return dateString;

    }

}
