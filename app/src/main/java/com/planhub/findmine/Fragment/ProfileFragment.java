package com.planhub.findmine.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.planhub.findmine.LoginActivity;
import com.planhub.findmine.MainActivity;
import com.planhub.findmine.R;
import com.planhub.findmine.SettingActivity;
import com.planhub.findmine.SetupProfileActivity;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private String currentUserId;
    private FirebaseFirestore firebaseFirestore;

    private Uri imgProfileURI = null;

    private TextView userNameProfile;
    private TextView userKelasProfile;
    private ImageView userImgProfile;

    // menambah action bar di profile
    public ActionBar getActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();

    }

    // menampilkan option menu di fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    // menampilkan option menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    // membuat function option menu di action bar profile
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                setting();
                break;
            case R.id.logout:
                logOut();
                break;
        }

        return true;

    }


    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        getActionBar().setTitle("Profile");

        userNameProfile = view.findViewById(R.id.tvNameProfile);
        userKelasProfile = view.findViewById(R.id.tvKelasProfile);
        userImgProfile = view.findViewById(R.id.imgProfile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // menampilkan detail profile
        if (mAuth.getCurrentUser() != null) {

            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {

                            String fullname = task.getResult().getString("name");
                            String kelas = task.getResult().getString("kelas");
                            String img_profile = task.getResult().getString("img_profile");

                            userNameProfile.setText(fullname);
                          userKelasProfile.setText(kelas);

                            RequestOptions placeholderReq = new RequestOptions();
                            placeholderReq.placeholder(R.drawable.upload_user_image);

                            Glide.with(getActivity()).setDefaultRequestOptions(placeholderReq).load(img_profile).into(userImgProfile);

                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(getActivity(), "Upss, firestore error " + errorMessage, Toast.LENGTH_SHORT).show();

                    }

                }

            });
        }

        return view;

    }



    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:
                logOut();
                return true;

            default:
                return false;


        }

    }*/

    private void logOut() {

        mAuth.signOut();
        Toast.makeText(getActivity(), "Anda berhasil keluar!", Toast.LENGTH_SHORT).show();
        sendToLogin();
    }

    private void sendToLogin() {

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }

    private void setting() {

        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

}
