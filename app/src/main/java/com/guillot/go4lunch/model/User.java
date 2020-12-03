package com.guillot.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.google.firebase.firestore.IgnoreExtraProperties;

import javax.annotation.Nullable;

@Keep
@IgnoreExtraProperties
public class User implements Parcelable {

    private String id;
    private String username;
    private String urlProfilePicture;
    private String userLocation;
    private String userMail;
    @Nullable
    private String restaurantId;
    @Nullable
    private String restaurantName;
    @Nullable
    private String restaurantAddress;
    private boolean isNotification;

    public User() {
    }

    public User(String id, String username, String urlProfilePicture, String userLocation, String userMail, String restaurantId, String restaurantName, String restaurantAddress, boolean isNotification) {
        this.id = id;
        this.username = username;
        this.urlProfilePicture = urlProfilePicture;
        this.userLocation = userLocation;
        this.userMail = userMail;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.isNotification = isNotification;
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        urlProfilePicture = in.readString();
        userLocation = in.readString();
        userMail = in.readString();
        restaurantId = in.readString();
        restaurantName = in.readString();
        restaurantAddress = in.readString();
        isNotification = in.readByte() != 0;
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

    @Nullable
    public String getRestaurantId() {
        return restaurantId;
    }

    @Nullable
    public String getRestaurantName() {
        return restaurantName;
    }

    @Nullable
    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public boolean isNotification() {
        return isNotification;
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
        dest.writeByte((byte) (isNotification ? 1 : 0));
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", urlPicture='" + urlProfilePicture + '\'' +
                ", userLocation='" + userLocation + '\'' +
                ", email='" + userMail + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", restaurantName=" + restaurantName + '\'' +
                ", restaurantAddress=" + restaurantAddress + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
