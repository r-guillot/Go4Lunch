package com.guillot.go4lunch.model;

import com.squareup.moshi.Json;

import java.util.List;

public class ApiResponse {

    @Json(name = "html_attributions")
    private List<Object> htmlAttributions;

    private List<RestaurantApi> results;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<RestaurantApi> getResults() {
        return results;
    }

    public void setResults(List<RestaurantApi> results) {
        this.results = results;
    }
}
