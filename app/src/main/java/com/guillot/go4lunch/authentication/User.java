package com.guillot.go4lunch.authentication;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class User implements Parcelable {
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

    public String getId() { return id; }
    public String getUsername() { return username; }
    public Uri getUrlProfilePicture() { return urlProfilePicture; }
    public LatLng getUserLocation() {return userLocation;}

    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setUrlProfilePicture(Uri urlProfilePicture) { this.urlProfilePicture = urlProfilePicture; }
    public void setUserLocation(LatLng userLocation) {this.userLocation = userLocation;}

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
    }
}
