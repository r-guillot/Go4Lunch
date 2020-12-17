package com.guillot.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private String restaurantID;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private int openingHours;
    private Integer distance;
    private String photoReference;
    private float rating;
    private String phoneNumber;
    private String webSite;
    private List<User> usersEatingHere;

    public Restaurant(String restaurantID, String name, Double latitude, Double longitude, @Nullable String address,
                      int openingHours, @Nullable String photoReference, @Nullable float rating, String phoneNumber, String webSite) {
        this.restaurantID = restaurantID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.openingHours = openingHours;
        this.photoReference = photoReference;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
        usersEatingHere = new ArrayList<>();
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(int openingHours) {
        this.openingHours = openingHours;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public void setUserGoingEating(List<User> users) {
        usersEatingHere = users;

    }

    public List<User> getUsersEatingHere() {
        return usersEatingHere;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

}
