package com.guillot.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantApi {

    @SerializedName("place_id")
    private String placeId;
    @SerializedName("name")
    private String name;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("photos")
    private List<Photos> photos;
    @SerializedName("rating")
    private float rating;
    @SerializedName("vicinity")
    private String vicinity;
    @SerializedName("website")
    private String website;
    @SerializedName("international_phone_number")
    private String phoneNumber;

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public float getRating() {
        return (3 * rating) / 5;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
