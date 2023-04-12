package com.example.challenge;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Character {
    private String name, status, species, origin, created;
    private String gender;
    private String imageUrl;
    private String location;
    private int character_id;
    private List<Integer> episodeIds;



    public Character(int character_id, String status, String species, String origin, String name, String gender, String imageUrl, String location, List<Integer> episodeIds,String created) {
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.location = location;
        this.status=status;
        this.species=species;
        this.origin=origin;
        this.character_id=character_id;
        this.episodeIds=episodeIds;
        this.created=created;
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

    public String getCreated(){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
        Date date = null;
        try {
            date = inputFormat.parse(this.created);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }
    public String getEpisodeIds()
    {
        String episode ="";
        if (!episodeIds.isEmpty()) {
            for (int i=0; i< episodeIds.size(); i++ ) {
                if(i==episodeIds.size()-1){
                    episode+=episodeIds.get(i);
                }else{ episode+=episodeIds.get(i)+ ", ";}

            }
        }
        return episode;

    }

}