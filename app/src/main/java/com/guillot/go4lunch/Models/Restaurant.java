package com.guillot.go4lunch.Models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    private String placeId;
    private Uri uriIcon;
    private String name;
    private double priceLevel;
    private double rating;
    private String address;

//    public Restaurant(String placeId, Uri uriIcon, String name,double priceLevel,double rating,String address) {
//        this.placeId = placeId;
//        this.uriIcon = uriIcon;
//        this.name = name;
//        this.priceLevel = priceLevel;
//        this.rating = rating;
//        this.address = address;
//    }

    //Getters//
    public String getPlaceId() { return placeId;
    }

    public Uri getUriIcon() { return uriIcon;
    }

    public String getName() { return name;
    }

    public double getPriceLevel() { return priceLevel;
    }

    public double getRating() { return rating;
    }

    public String getAddress() { return address;
    }

//    //Setters//
//    public void setPlaceId(String placeId) { this.placeId = placeId;
//    }
//
//    public void setUriIcon(Uri uriIcon) { this.uriIcon = uriIcon;
//    }
//
//    public void setName(String name) { this.name = name;
//    }
//
//    public void setPriceLevel(double priceLevel) { this.priceLevel = priceLevel;
//    }
//
//    public void setRating(double rating) { this.rating = rating;
//    }
//
//    public void setAddress(String address) { this.address = address;
//    }
}
