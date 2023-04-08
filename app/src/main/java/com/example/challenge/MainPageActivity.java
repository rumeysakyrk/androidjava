package com.example.challenge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.challenge.CharacterAdapter;

public class MainPageActivity extends AppCompatActivity implements LocationAdapter.LocationAdapterListener{

    private RecyclerView recycler_view;
    private RecyclerView vertical_recycler_view;
    private LocationAdapter locationAdapter;
    private CharacterAdapter characterAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view = findViewById(R.id.recycler_view);

        recycler_view.setLayoutManager(layoutManager);

        locationAdapter = new LocationAdapter(this,this);
        recycler_view.setAdapter(locationAdapter);

        // ActionBar background color
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF00FF00"));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable);
        }

        new GetLocationsTask().execute();
        vertical_recycler_view = findViewById(R.id.vertical_recycler_view);

        characterAdapter = new CharacterAdapter(this);
        vertical_recycler_view.setAdapter(characterAdapter);
    }

    @Override
    public void onLocationSelected(String location) {
        characterAdapter.clearCharacters();
        // Seçilen lokasyona ait karakterler listeye eklenir
        characterAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetLocationsTask extends AsyncTask<Void, Void, List<String>>  {

        @Override
        protected List<String> doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://rickandmortyapi.com/api/location");
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

                    return getLocationNamesFromResponse(response.toString());
                }

                conn.disconnect();
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
        protected void onPostExecute(List<String> locationNames) {
            super.onPostExecute(locationNames);
            if (locationNames == null) {
                locationNames = new ArrayList<>(); // Null yerine boş bir liste atıyoruz.
            }
            locationAdapter.setLocationNames((ArrayList<String>) locationNames);

            String selectedLocation = locationAdapter.getSelectedLocation();

            String locationUrl;
            if (selectedLocation != null) {
                locationUrl = "https://rickandmortyapi.com/api/character?location=" + selectedLocation;
            } else {
                locationUrl = "https://rickandmortyapi.com/api/character";
            }
            new GetCharactersTask(locationUrl).execute();
        }

        private List<String> getLocationNamesFromResponse(String response) {
            List<String> locationNames = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length() && i < 20; i++) {
                    JSONObject locationObject = jsonArray.getJSONObject(i);
                    String name = locationObject.getString("name");
                    locationNames.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return locationNames;
        }

    }


    private class GetCharactersTask extends AsyncTask<Void, Void, List<Character>> {
        private String locationUrl;

        public GetCharactersTask(String locationUrl) {
            this.locationUrl = locationUrl;
        }

        @Override
        protected List<Character> doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            try {
                String locationUrl = "https://rickandmortyapi.com/api/character";
                if (locationAdapter.getSelectedLocation() != null) {
                    locationUrl += "?location=" + locationAdapter.getSelectedLocation();
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
            super.onPostExecute(characters);
            if (characterAdapter != null) {
                characterAdapter.setCharacters(characters);
            }
        }

        private List<Character> getCharactersFromResponse(String response) {
            List<Character> characters = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length() && i < 20; i++) {
                    JSONObject characterObject = jsonArray.getJSONObject(i);
                    int id = characterObject.getInt("id");
                    String name = characterObject.getString("name");
                    String gender = characterObject.getString("gender");
                    String imageUrl = characterObject.getString("image");

                    Character character = new Character(id, name, gender, imageUrl);
                    characters.add(character);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return characters;
        }
    }
}