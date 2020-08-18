package com.guillot.go4lunch.authentication;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class User {
    private String id;
    private String username;
    private Uri urlProfilePicture;
    private LatLng userLocation;

    public User(String id, String username, Uri urlProfilePicture, LatLng userLocation) {
        this.id = id;
        this.username = username;
        this.urlProfilePicture = urlProfilePicture;
        this.userLocation = userLocation;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public Uri getUrlProfilePicture() { return urlProfilePicture; }
    public LatLng getUserLocation() {return userLocation;}

    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setUrlProfilePicture(Uri urlProfilePicture) { this.urlProfilePicture = urlProfilePicture; }
    public void setUserLocation(LatLng userLocation) {this.userLocation = userLocation;}
}
