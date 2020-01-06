package com.planhub.findmine.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.planhub.findmine.Adapter.SearchAdapter;
import com.planhub.findmine.Model.Post;
import com.planhub.findmine.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView rvPostSearch;
    private RecyclerView mResultList;
    private List<Post> searchList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private DocumentSnapshot lastVisibe;

    private SearchAdapter searchAdapter;

    public Context context;

    private static final String TAG = "HomeFragment";

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        searchList = new ArrayList<>();
        rvPostSearch = view.findViewById(R.id.rvPostSearch);

        mSearchField = view.findViewById(R.id.edtSearchItem);
        mSearchBtn = view.findViewById(R.id.btnSearch);

        mAuth = FirebaseAuth.getInstance();

        // mengatur recycler view
        searchAdapter = new SearchAdapter(searchList);
        rvPostSearch.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rvPostSearch.setAdapter(searchAdapter);
        rvPostSearch.setHasFixedSize(true);

        // edit text search
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });


        if (mAuth.getCurrentUser() != null) {
            // menampilkan data pada tabel post di firebase firestore
            firebaseFirestore = FirebaseFirestore.getInstance();

            rvPostSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(-1);

                    if (reachedBottom) {

                        loadMoreQuery();

                    }
                }
            });

            Query query = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(3);

            query.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                    lastVisibe = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                    // menampilkan jika hanya ada data di database
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            Post post = doc.getDocument().toObject(Post.class);
                            searchList.add(post);

                            searchAdapter.notifyDataSetChanged();
                        }

                    }
                }

            });
        }
        return view;
    }

    private void loadMoreQuery() {

        if (mAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisibe)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        // menampilkan jika hanya ada data di database
                        lastVisibe = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                Post post = doc.getDocument().toObject(Post.class);
                                searchList.add(post);

                                searchAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }

            });
        }

    }

    // filtering title
    private void filter(String text) {

        List<Post> filterTitle = new ArrayList<>();

        for (Post s : searchList) {

            if (s.getTitle().toLowerCase().contains(text.toLowerCase())) {

                filterTitle.add(s);

            }
        }

        searchAdapter.filterList(filterTitle);

    }

}
