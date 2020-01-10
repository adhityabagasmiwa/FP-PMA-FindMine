package com.planhub.findmine.Model;

public class User {

    public String imgDetailProfile, nameUser;

    public User() {

    }

    public String getImgDetailProfile() {
        return imgDetailProfile;
    }

    public void setImgDetailProfile(String imgDetailProfile) {
        this.imgDetailProfile = imgDetailProfile;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public User(String imgDetailProfile, String nameUser) {
        this.imgDetailProfile = imgDetailProfile;
        this.nameUser = nameUser;
    }
}
