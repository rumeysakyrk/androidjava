package com.example.challenge;

public class Location {
    private String name;
    private String type;
    private String dimension;
    private String locationName;


    public Location(String name, String type, String dimension) {
        this.name = name;
        this.type = type;
        this.dimension = dimension;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDimension() {
        return dimension;
    }
}
