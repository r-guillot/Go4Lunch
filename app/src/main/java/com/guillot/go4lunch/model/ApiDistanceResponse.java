package com.guillot.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApiDistanceResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("rows")
    public List<InfoDistanceMatrix> rows;

    public class InfoDistanceMatrix {
        @SerializedName("elements")
        public List elements;

        public class DistanceElement {
            @SerializedName("status")
            public String status;
            @SerializedName("duration")
            public ValueItem duration;
            @SerializedName("distance")
            public ValueItem distance;
        }

        public class ValueItem {
            @SerializedName("value")
            public long value;
            @SerializedName("text")
            public String text;

        }
    }
}
