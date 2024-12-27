package com.example.filefolderrename.models;

import android.net.Uri;

import java.util.Date;

public class PictureFacer implements Comparable<PictureFacer> {


    private String picturName;
    private String picturePath;
    private String pictureSize;
    private String imageUri;
    private Uri uri;
    private String type;


    private Date dateTime;


    public PictureFacer() {

    }

    public PictureFacer(String picturName, String picturePath, String pictureSize, String imageUri, String type) {
        this.picturName = picturName;
        this.picturePath = picturePath;
        this.pictureSize = pictureSize;
        this.imageUri = imageUri;
        this.type = type;
    }


    public String getPicturName() {
        return picturName;
    }

    public void setPicturName(String picturName) {
        this.picturName = picturName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int compareTo(PictureFacer o) {
        return 0;
    }
}
   

