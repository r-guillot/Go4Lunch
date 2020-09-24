package com.guillot.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {

    @SerializedName("html_attributions")
    private List<Object> htmlAttribution = null;
    @SerializedName("photo_reference")
    private String photoReference;
    private int width;
    private int height;

    public List<Object> getHtmlAttribution() {
        return htmlAttribution;
    }

    public void setHtmlAttribution(List<Object> htmlAttribution) {
        this.htmlAttribution = htmlAttribution;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Photos{" +
                "photoReference='" + photoReference + '\'' +
                ", width=" + width +
                '}';
    }
}
