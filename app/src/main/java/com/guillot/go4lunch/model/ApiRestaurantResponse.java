package com.guillot.go4lunch.model;

import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

public class ApiRestaurantResponse {

    @SerializedName("results")
    private List<RestaurantApi> results;

    @SerializedName("status")
    private String status;

    public List<RestaurantApi> getResults() {
        return results;
    }

    public void setResults(List<RestaurantApi> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
