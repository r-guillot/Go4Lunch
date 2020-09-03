package com.guillot.go4lunch.Restaurant;

import com.squareup.moshi.Json;

import java.util.List;

class DetailReponse {

    @Json(name = "html_attributions")
    private List<Object> htmlAttributions;

    private List<Restaurant> results;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<Restaurant> getResults() {
        return results;
    }

    public void setResults(List<Restaurant> results) {
        this.results = results;
    }
}
