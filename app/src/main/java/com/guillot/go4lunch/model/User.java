package com.guillot.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

import javax.annotation.Nullable;

@Keep
@IgnoreExtraProperties
public class User implements Parcelable{

    private String id;
    private String username;
    private String urlProfilePicture;
    private String userLocation;
    private String userMail;
    private List<String> restaurantLiked;
    private String restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private boolean notification;

    public User() {
    }

    public User(String id, String username, String urlProfilePicture, String userLocation, String userMail, List<String> restaurantLiked, String restaurantId, String restaurantName, String restaurantAddress, boolean notification) {
        this.id = id;
        this.username = username;
        this.urlProfilePicture = urlProfilePicture;
        this.userLocation = userLocation;
        this.userMail = userMail;
        this.restaurantLiked = restaurantLiked;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.notification = notification;
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        urlProfilePicture = in.readString();
        userLocation = in.readString();
        userMail = in.readString();
        restaurantLiked = in.createStringArrayList();
        restaurantId = in.readString();
        restaurantName = in.readString();
        restaurantAddress = in.readString();
        notification = in.readByte() != 0;
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

    public String getUrlProfilePicture() {
        return urlProfilePicture;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public String getUserMail() {
        return userMail;
    }

    public List<String> getRestaurantLiked() {
        return restaurantLiked;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setUrlProfilePicture(String urlProfilePicture) {
        this.urlProfilePicture = urlProfilePicture;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public void setRestaurantId(@Nullable String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(@Nullable String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(@Nullable String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantLiked(List<String> restaurantLiked) {
        this.restaurantLiked = restaurantLiked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(urlProfilePicture);
        dest.writeString(userLocation);
        dest.writeString(userMail);
        dest.writeString(restaurantId);
        dest.writeString(restaurantName);
        dest.writeString(restaurantAddress);
    }
}
