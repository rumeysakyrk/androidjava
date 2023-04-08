package com.example.challenge;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationResponse {
    @SerializedName("results")
    private List<Location> locationList;

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }
}
