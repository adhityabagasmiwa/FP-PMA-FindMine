package com.planhub.findmine.Model;

import java.util.Date;
import com.google.firebase.firestore.ServerTimestamp;

public class Post extends postId {

    private String id_user, title, desc, img_url, img_profile;
    private Date timestamp;

    public Post() {

    }

    public Post(String id_user, String title, String desc, String img_url, String img_profile, Date timestamp) {
        this.id_user = id_user;
        this.title = title;
        this.desc = desc;
        this.img_url = img_url;
        this.timestamp = timestamp;
        this.img_profile = img_profile;
    }

    public Post(String img_profile) {
        this.img_profile = img_profile;
    }

    public String getImg_profile() {
        return img_profile;
    }

    public void setImg_profile(String img_profile) {
        this.img_profile = img_profile;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
