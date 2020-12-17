package com.guillot.go4lunch.model.distance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiDistanceResponse {
    @SerializedName("destination_addresses")
    private List<String> destinationAddresses = null;
    @SerializedName("origin_addresses")
    private List<String> originAddresses = null;
    @SerializedName("rows")
    private List<Row> rows;
    @SerializedName("status")
    private String status;

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
