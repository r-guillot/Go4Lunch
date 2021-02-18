package com.guillot.go4lunch.model.distance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Row {

    @SerializedName("elements")
    private List<Elements> elements = null;

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }
}
