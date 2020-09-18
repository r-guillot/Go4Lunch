package com.guillot.go4lunch.Restaurant.model;

import android.net.Uri;

import com.squareup.moshi.Json;

import java.util.List;

public class Restaurant {
    @Json(name = "place_id")
    private String placeId;
    @Json(name = "icon")
    private Uri uriIcon;
    @Json(name = "name")
    private String name;
    private Geometry geometry;
    private List<Photos> photos;
    private float rating;
    private String vicinity;

    public Restaurant(String placeId, Uri uriIcon, String name,
                      Geometry geometry, List<Photos> photos, float rating, String vicinity
    ) {
        this.placeId = placeId;
        this.uriIcon = uriIcon;
        this.name = name;
        this.geometry = geometry;
        this.photos = photos;
        this.rating = rating;
        this.vicinity = vicinity;
    }


    //Getters//
    public String getPlaceId() {
        return placeId;
    }

    public Uri getUriIcon() {
        return uriIcon;
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

    public float getRating() { return rating; }

    public String getVicinity() { return vicinity; }



    public float ratingOnThree() {
        float result = (3 * rating)/5;
        return result;
    }
}



