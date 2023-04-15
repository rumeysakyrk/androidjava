package com.example.challenge;


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

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private static List<Character> characters;
    private static Context context;

    public CharacterAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
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
                    URL url = new URL(locationUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
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
                CharacterAdapter.this.characters.clear();
                notifyDataSetChanged();
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
                String created=characterObject.getString("created");
                List<Integer> episodeIds=new ArrayList<>();
                JSONArray episodeUrls = characterObject.getJSONArray("episode");
                for (int j = 0; j < episodeUrls.length(); j++) {
                    try {
                        episodeIds.add(Integer.parseInt(episodeUrls.getString(j).split("episode/")[1]));
                    } catch (JSONException e) {e.printStackTrace();}
                }

                Character character = new Character(id, status, species, origin,name, gender, imageUrl,locationName,episodeIds,created);
                characters.add(character);
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
        ImageView genderIcon = holder.itemView.findViewById(R.id.character_icon);
        if (character.getGender().equals("Male")) {
            genderIcon.setImageResource(R.drawable.male);
        } else if (character.getGender().equals("Female")) {
            genderIcon.setImageResource(R.drawable.female);
        } else {
            genderIcon.setImageResource(R.drawable.unknown);
        }

        Picasso.get().load(character.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }


    public static class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView statusTextView;
        public TextView speciesTextView;
        public TextView originTextView;
        public TextView nameTextView;
        public TextView genderTextView;
        public TextView locationTextView;
        public TextView createdTextView;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.character_image);
            nameTextView = itemView.findViewById(R.id.character_name);
            genderTextView = itemView.findViewById(R.id.character_gender);
            statusTextView=itemView.findViewById(R.id.character_status);
            speciesTextView=itemView.findViewById(R.id.character_species);
            originTextView=itemView.findViewById(R.id.character_origin);
            locationTextView=itemView.findViewById(R.id.location);
            createdTextView=itemView.findViewById(R.id.createdAt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Character character = characters.get(position);
            Intent intent = new Intent(context, DetailPageActivity.class);
            intent.putExtra("character_id", character.getId());
            intent.putExtra("name", character.getName());
            intent.putExtra("gender", character.getGender());
            intent.putExtra("origin", character.getOrigin());
            intent.putExtra("status", character.getStatus());
            intent.putExtra("species", character.getSpecies());
            intent.putExtra("episode", character.getEpisodeIds());
            intent.putExtra("location", character.getLocation());
            intent.putExtra("created", character.getCreated());
            intent.putExtra("imageUrl", character.getImageUrl());
            context.startActivity(intent);
        }

    }

}