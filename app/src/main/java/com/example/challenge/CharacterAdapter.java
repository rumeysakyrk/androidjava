package com.example.challenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private static List<Character> characters;
    private List<Character> characters_org;
    private static Context context;

    public CharacterAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    public void setCharacters(List<Character> characters) {
        if (characters == null) {
            this.characters = new ArrayList<>();
        } else {
            this.characters = characters;
        }
        notifyDataSetChanged();
    }

    public void filterCharacters(Location location) {
        new GetCharactersTask().execute(location);
    }

    private class GetCharactersTask extends AsyncTask<Location, Void, List<Character>> {
        @Override
        protected List<Character> doInBackground(Location... locations) {
            HttpURLConnection conn = null;
            try {
                Location location = locations[0];
                String locationUrl = "https://rickandmortyapi.com/api/character/";
                if (!location.residentIds.isEmpty()) {
                    for (Integer id : location.residentIds) {
                        locationUrl += id + ",";
                    }

                    System.out.println(locationUrl);
                    URL url = new URL(locationUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    System.out.println("----------");
                    System.out.println(conn.getResponseCode());
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        conn.disconnect();
                        return getCharactersFromResponse(response.toString());
                    }
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Character> characters) {
            if (characters != null) {
                // Update the adapter with the new character data
                CharacterAdapter.this.characters = characters;
                notifyDataSetChanged();
            } else {
                // Handle the case where the API request failed
            }
        }
    }
    private static List<Character> getCharactersFromResponse(String response) {
        List<Character> characters = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject characterObject = jsonArray.getJSONObject(i);
                int id = characterObject.getInt("id");
                String name = characterObject.getString("name");
                String gender = characterObject.getString("gender");
                String imageUrl = characterObject.getString("image");
                String locationName = characterObject.getJSONObject("location").getString("name");

                String status= characterObject.getString("status");
                String origin=characterObject.getJSONObject("origin").getString("name");
                String species=characterObject.getString("species");
                Character character = new Character(id, status, species, origin,name, gender, imageUrl,locationName);
                characters.add(character);
                for (Character item:characters
                ) {
                    System.out.println(item.getLocation());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return characters;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.character_item, parent, false);
        return new CharacterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = characters.get(position);
        holder.nameTextView.setText(character.getName());
        holder.genderTextView.setText(character.getGender());
        Picasso.get().load(character.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void clearCharacters() {
        characters.clear();
        notifyDataSetChanged();
    }


    public static class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView statusTextView;
        public TextView speciesTextView;
        public TextView originTextView;
        public TextView nameTextView;
        public TextView genderTextView;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.character_image);
            nameTextView = itemView.findViewById(R.id.character_name);
            genderTextView = itemView.findViewById(R.id.character_gender);
            statusTextView=itemView.findViewById(R.id.character_status);
            speciesTextView=itemView.findViewById(R.id.character_species);
            originTextView=itemView.findViewById(R.id.character_origin);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Character character = characters.get(position);
            Intent intent = new Intent(context, DetailPageActivity.class);
            intent.putExtra("character_id", character.getId());
            intent.putExtra("name", character.getName());
            intent.putExtra("gender", "Gender: " + character.getGender());
            intent.putExtra("origin", "Origin: " + character.getOrigin());
            intent.putExtra("status", "Status: " + character.getStatus());
            intent.putExtra("species", "Species: " + character.getSpecies());
            intent.putExtra("imageUrl", character.getImageUrl());
            context.startActivity(intent);
        }

    }

}