package com.guillot.go4lunch.model.details;

import com.google.gson.annotations.SerializedName;

public class OpeningPeriods {
    @SerializedName("close")
    private CloseHour closeHour;
    @SerializedName("open")
    private OpenHour openHour;

    public CloseHour getCloseHour() {
        return closeHour;
    }

    public OpenHour getOpenHour() {
        return openHour;
    }
}
