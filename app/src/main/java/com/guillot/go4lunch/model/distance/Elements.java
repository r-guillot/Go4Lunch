package com.guillot.go4lunch.model.distance;

import com.google.gson.annotations.SerializedName;

public class Elements {

    @SerializedName("distance")
    private Distance distance;
    @SerializedName("status")
    private String status;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
