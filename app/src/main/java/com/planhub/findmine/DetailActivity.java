package com.planhub.findmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.planhub.findmine.Adapter.PostAdapter;
import com.planhub.findmine.Model.Post;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        if (mAuth.getCurrentUser() != null) {

            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        String fullname = task.getResult().getString("name");
                        String kelas = task.getResult().getString("kelas");
                        String img_profile = task.getResult().getString("img_profile");

                        tvDetailUsername.setText(fullname);

                        getData(img_profile);

                    }

                }
            });

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.share_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.sharePict:
                shareContent();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareContent() {
        Bitmap bitmap =getBitmapFromView(imgPostDetail);
        try {
            File file =new File(this.getExternalCacheDir(),"test.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
           /* final Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(tvDetailTitle));
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent,"share information via"));*/

           final Intent intent = new Intent(Intent.ACTION_SEND);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent.putExtra(Intent.EXTRA_TEXT, "judul informasi");
           intent.putExtra(Intent.EXTRA_STREAM, "gambar barang");
           intent.setType("image/png");
           startActivity(Intent.createChooser(intent,"sharethis"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view){
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null){
            bgDrawable.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

}
