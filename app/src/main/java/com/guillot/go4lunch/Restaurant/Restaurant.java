package com.guillot.go4lunch.Restaurant;

import android.net.Uri;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    @Json(name = "place_id")
    private String placeId;
    @Json(name ="icon")
    private Uri uriIcon;
    @Json(name ="name")
    private String name;
//    private double priceLevel;
//    private double rating;
//    private String address;
    private Geometry geometry;

    public List<Restaurant> restaurantList;

    public Restaurant(String placeId, Uri uriIcon, String name,
//                      double priceLevel,
//                      double rating, String address,
                      Geometry geometry
    ) {
        this.placeId = placeId;
        this.uriIcon = uriIcon;
        this.name = name;
        this.geometry = geometry;
//        this.priceLevel = priceLevel;
//        this.rating = rating;
//        this.address = address;
    }

    public Restaurant(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    //Getters//
    public String getPlaceId() { return placeId;
    }

    public Uri getUriIcon() { return uriIcon;
    }

    public String getName() { return name;
    }
//
//    public double getPriceLevel() { return priceLevel;
//    }
//
//    public double getRating() { return rating;
//    }
//
//    public String getAddress() { return address;
//    }
//

    public Geometry getGeometry() {
        return geometry;
    }

//
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
