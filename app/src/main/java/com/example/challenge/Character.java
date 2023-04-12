package com.example.challenge;


public class Character {
    private String name, status, species, origin;
    private String gender;
    private String imageUrl;
    private String location;
    private int character_id;


    public Character(int character_id, String status, String species, String origin, String name, String gender, String imageUrl, String location) {
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.location = location;
        this.status=status;
        this.species=species;
        this.origin=origin;
        this.character_id=character_id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }
    public String getSpecies() {
        return species;
    }
    public String getOrigin() {
        return origin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return character_id;
    }

}