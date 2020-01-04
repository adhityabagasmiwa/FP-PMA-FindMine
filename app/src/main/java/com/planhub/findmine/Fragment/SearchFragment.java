package com.planhub.findmine.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
    private List<Post> postList;

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

        postList = new ArrayList<>();
        rvPostSearch = view.findViewById(R.id.rvPostSearch);

        mSearchField = view.findViewById(R.id.edtSearchItem);
        mSearchBtn = view.findViewById(R.id.btnSearch);

        mAuth = FirebaseAuth.getInstance();

        // mengatur recycler view
        searchAdapter = new SearchAdapter(postList);
        rvPostSearch.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rvPostSearch.setAdapter(searchAdapter);
        rvPostSearch.setHasFixedSize(true);

        /*mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                firebaseSearch(searchText);

            }
        });*/

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
                            postList.add(post);

                            searchAdapter.notifyDataSetChanged();
                        }

                    }
                }

            });
        }
        return view;
    }

    /*private void firebaseSearch(String searchText) {

        Query query = FirebaseFirestore.getInstance()
                .collection("Posts")
                .orderBy("timestamp")
                .limit(50);

        FirestoreRecyclerAdapter<Post, UsersViewHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Post, UsersViewHolder>(Post.class,R.layout.item_post_search,UsersViewHolder.class) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Post model) {
                holder.setDetails(context, model.getTitle(), model.getDesc(), model.getImg_url());

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_search, parent, false);
                context = parent.getContext();
                firebaseFirestore = FirebaseFirestore.getInstance();
                return view;
            }
        };

        mResultList.setAdapter(firestoreRecyclerAdapter);

    }*/

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
                                postList.add(post);

                                searchAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }

            });
        }

    }

    // View Holder Class

    /*public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String titlePost, String descPost, String imgPostSearch){

            TextView title = mView.findViewById(R.id.tvTitleSearch);
            TextView desc = mView.findViewById(R.id.tvDescSearch);
            ImageView imgPost = mView.findViewById(R.id.imgPostSearch);


            title.setText(titlePost);
            desc.setText(imgPostSearch);

            Glide.with(ctx).load(imgPostSearch).into(imgPost);


        }

    }*/

}
