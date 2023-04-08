package com.example.challenge;

public class Character {
    private String name;
    private String gender;
    private String imageUrl;

    public Character(int id, String name, String gender, String imageUrl) {
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
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
}

