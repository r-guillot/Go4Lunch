package com.guillot.go4lunch.Restaurant.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Photos {
    private int height;
    @Json(name ="html_attributions")
    private List<Object> htmlAttribution = null;
    @Json(name = "photo_reference")
    public String photoReference;
    private int width;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
}
