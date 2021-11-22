package com.wispr.wispr.mainPages;

import android.graphics.Bitmap;

import com.wispr.wispr.adapter.SuperEntity;


public class GalleryItem extends SuperEntity {

    String fileURL;
    int fileType;
    Bitmap bitmap;
    boolean isMarked;

    public GalleryItem(String fileURL, Bitmap bitmap, int fileType, boolean isMarked) {
        this.fileURL = fileURL;
        this.fileType = fileType;
        this.bitmap=bitmap;
        this.isMarked=isMarked;
    }


    public String getFileURL() {
        return fileURL;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getFileType() {
        return fileType;
    }

    public boolean isMarked() {
        return isMarked;
    }
}
