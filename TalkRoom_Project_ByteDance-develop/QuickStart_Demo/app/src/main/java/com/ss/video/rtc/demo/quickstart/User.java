package com.ss.video.rtc.demo.quickstart;

import android.view.TextureView;
import android.widget.FrameLayout;

public class User {
    public TextureView mView;
    public FrameLayout.LayoutParams mParams;
    public String mUid;
    public int mWidth;
    public int mHeight;

    public User(TextureView mView, FrameLayout.LayoutParams mParams, String mUid){
        this.mView = mView;
        this.mParams = mParams;
        this.mUid = mUid;
        this.mWidth = 500;
        this.mHeight = 500;
    }

    public void setmUid(String newUid) {this.mUid = newUid;}
    public void setmWidth(int width){
        this.mWidth = width;
    }

    public void setmHeight(int height){
        this.mHeight = height;
    }
}
