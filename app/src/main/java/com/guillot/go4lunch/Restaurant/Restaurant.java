package com.guillot.go4lunch.Restaurant;

import android.net.Uri;

public class Restaurant {
    private String place_id;
    private Uri uriIcon;
    private String name;
    private double priceLevel;
    private double rating;
    private String address;
    private double lat;
    private double lng;

    public Restaurant(String place_id, Uri uriIcon, String name, double priceLevel,
                      double rating, String address, double lat, double lng) {
        this.place_id = place_id;
        this.uriIcon = uriIcon;
        this.name = name;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    //Getters//
    public String getPlace_id() { return place_id;
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

    public double getLat() { return lat;
    }

    public double getLng() { return lng;
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
