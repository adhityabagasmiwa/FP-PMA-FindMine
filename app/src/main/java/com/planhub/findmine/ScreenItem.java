package com.planhub.findmine;

public class ScreenItem {

    String Title, Desc;
    int screenImage;

    public ScreenItem(String title, String desc, int screenImage) {
        Title = title;
        Desc = desc;
        this.screenImage = screenImage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getScreenImage() {
        return screenImage;
    }

    public void setScreenImage(int screenImage) {
        this.screenImage = screenImage;
    }
}
