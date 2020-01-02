package com.planhub.findmine.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.planhub.findmine.Model.Post;
import com.planhub.findmine.R;

import java.util.Date;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context context;
    public List<Post> postList;


    private FirebaseUser firebaseDatabase;

    public PostAdapter(List<Post> postList) {

        this.postList = postList;

    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        String titleData = postList.get(position).getTitle();
        holder.setTitleText(titleData);

        String descData = postList.get(position).getDesc();
        holder.setDescText(descData);

        String img_url = postList.get(position).getImg_url();
        holder.setImgPost(img_url);

        String img_profile = postList.get(position).getImg_profile();
        holder.setImgProfile(img_profile);

        String id_user = postList.get(position).getId_user();

        long millisecond = postList.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MMM d, hh:mm a", new Date(millisecond)).toString();
        holder.setTime(dateString);


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle, mDesc, mDate;
        private ImageView mImgPost, mImgProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitleText(String titleText) {

            mTitle = mView.findViewById(R.id.tvTitlePost);
            mTitle.setText(titleText);

        }

        public void setImgPost(String imgDownloadURI) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.post_placeholder);

            mImgPost = mView.findViewById(R.id.imgPost);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imgDownloadURI).into(mImgPost);

        }

        public void setImgProfile(String imgProfileURI) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);

            mImgProfile = mView.findViewById(R.id.imgUserPost);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imgProfileURI).into(mImgProfile);

        }

        public void setDescText(String descText) {

            mDesc = mView.findViewById(R.id.tvDescPost);
            mDesc.setText(descText);

        }

        public void setTime(String date) {

            mDate = mView.findViewById(R.id.tvDatePost);
            mDate.setText(date);

        }
    }
}

