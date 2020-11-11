package com.guillot.go4lunch.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import javax.annotation.Nullable;

public class User implements Parcelable {

    private String id;
    private String username;
    private Uri urlProfilePicture;
    private LatLng userLocation;
    private String userMail;
    @Nullable
    private String restaurantId;
    @Nullable
    private String restaurantName;


    public User(String id, String username, Uri urlProfilePicture, LatLng userLocation, String userMail, String restaurantId, String restaurantName) {
        this.id = id;
        this.username = username;
        this.urlProfilePicture = urlProfilePicture;
        this.userLocation = userLocation;
        this.userMail = userMail;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        urlProfilePicture = in.readParcelable(Uri.class.getClassLoader());
        userLocation = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Uri getUrlProfilePicture() {
        return urlProfilePicture;
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public String getUserMail() {
        return userMail;
    }

    @Nullable
    public String getRestaurantId() {
        return restaurantId;
    }

    @Nullable
    public String getRestaurantName() {
        return restaurantName;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrlProfilePicture(Uri urlProfilePicture) {
        this.urlProfilePicture = urlProfilePicture;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public void setRestaurantId(@Nullable String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(@Nullable String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        urlProfilePicture.writeToParcel(dest, flags);
        dest.writeParcelable(userLocation, flags);
        dest.writeString(restaurantId);
    }
}
