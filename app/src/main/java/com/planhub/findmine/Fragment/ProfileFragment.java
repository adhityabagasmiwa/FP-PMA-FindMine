package com.planhub.findmine.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.planhub.findmine.LoginActivity;
import com.planhub.findmine.R;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button btnLogOut;

    private Uri imgProfileURI = null;
    private String idUser;
    private String userNameProfile;
    private ImageView userImgProfile;


    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        View v = getLayoutInflater().inflate(R.layout.fragment_profile,null);
        Toolbar toolbar = v.findViewById(R.id.profileToolbar);
        toolbar.setTitle("Profile");

        /*Toolbar toolbar = getActivity().findViewById(R.id.profileToolbar);
        toolbar.setTitle("Profile");*/


        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())// on run time get id what button os click and get id
        {
            case R.id.btnLogOut:
                logOut();
                break;
        }
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


}
