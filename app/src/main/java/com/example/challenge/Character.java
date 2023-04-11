package com.example.challenge;


public class Character {
    private String name;
    private String gender;
    private String imageUrl;
    private String location;


    public Character(int id, String name, String gender, String imageUrl, String location) {
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocation() {
        return location;
    }
}