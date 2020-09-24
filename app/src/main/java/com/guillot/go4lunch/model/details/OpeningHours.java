package com.guillot.go4lunch.model.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {
    @SerializedName("open_now")
    private Boolean openNow;
    @SerializedName("periods")
    private List<OpeningPeriods> openingPeriods;

    public Boolean getOpenNow() {
        return openNow;
    }

    public List<OpeningPeriods> getOpeningPeriods() {
        return openingPeriods;
    }
}
