package com.planhub.findmine.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.planhub.findmine.Adapter.PostAdapter;
import com.planhub.findmine.Model.Post;
import com.planhub.findmine.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Post> postList;

    private FirebaseFirestore firebaseFirestore;
    private PostAdapter postAdapter;

    private static final String TAG = "HomeFragment";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        postList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvPost);

        // mengatur recycler view
        postAdapter = new PostAdapter(postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(postAdapter);
        recyclerView.setHasFixedSize(true);

        // menampilkan data pada tabel post di firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {


                // menampilkan jika hanya ada data di database
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Post post = doc.getDocument().toObject(Post.class);
                        postList.add(post);

                        postAdapter.notifyDataSetChanged();
                    }

                }
            }

        });


        return view;

    }

}
