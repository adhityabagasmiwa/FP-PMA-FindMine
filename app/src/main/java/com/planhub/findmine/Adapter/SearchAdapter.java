package com.planhub.findmine.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.planhub.findmine.DetailActivity;
import com.planhub.findmine.Model.Post;
import com.planhub.findmine.R;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public Context context;
    public List<Post> searchList;

    private FirebaseFirestore firebaseFirestore;

    public SearchAdapter(List<Post> searchList) {

        this.searchList = searchList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_search, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new SearchAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String titleData = searchList.get(position).getTitle();
        holder.setTitleText(titleData);

        String descData = searchList.get(position).getDesc();
        holder.setDescText(descData);

        String img_url = searchList.get(position).getImg_url();
        holder.setImgPost(img_url);

        long millisecond = searchList.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MMM d, hh:mm a", new Date(millisecond)).toString();
        holder.setTime(dateString);

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle, mDesc, mDate;
        private ImageView mImgPost, mImgProfile;

        private TextView userNamePost;
        private CircleImageView userImgPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            // menampilkan detail barang
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DetailActivity.class);
                    int position = getAdapterPosition();

                    intent.putExtra("title", searchList.get(position).getTitle());
                    intent.putExtra("img_url", searchList.get(position).getImg_url());
                    intent.putExtra("desc", searchList.get(position).getDesc());
                    /*intent.putExtra("img_profile", postList.get(position).getImg_profile());*/

                    long millisecond = searchList.get(position).getTimestamp().getTime();
                    intent.putExtra("timestamp", millisecond);
                    context.startActivity(intent);

                }
            });

        }

        public void setTitleText(String titleText) {

            mTitle = mView.findViewById(R.id.tvTitleSearch);
            mTitle.setText(titleText);

        }

        public void setImgPost(String imgDownloadURI) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.post_placeholder);

            mImgPost = mView.findViewById(R.id.imgPostSearch);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imgDownloadURI).into(mImgPost);

        }

        /*public void setImgProfile(String imgProfileURI) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);

            mImgProfile = mView.findViewById(R.id.imgPostSearch);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imgProfileURI).into(mImgProfile);

        }*/

        public void setDescText(String descText) {

            mDesc = mView.findViewById(R.id.tvDescSearch);
            mDesc.setText(descText);

        }

        public void setTime(String date) {

            mDate = mView.findViewById(R.id.tvDateSearch);
            mDate.setText(date);

        }

        /*public void setUserData(String name, String img_profile) {

            userNamePost = mView.findViewById(R.id.tvNameUserPost);
            userImgPost = mView.findViewById(R.id.imgUserPost);

            userNamePost.setText(name);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);
            Glide.with(context.getApplicationContext()).applyDefaultRequestOptions(requestOptions).load(img_profile).into(userImgPost);

        }
*/

    }
}
