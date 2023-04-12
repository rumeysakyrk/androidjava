package com.example.challenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view = findViewById(R.id.recycler_view);

        recycler_view.setLayoutManager(layoutManager);

        locationAdapter = new LocationAdapter(this,this);
        recycler_view.setAdapter(locationAdapter);

        new GetLocationsTask().execute();
        vertical_recycler_view = findViewById(R.id.vertical_recycler_view);

        characterAdapter = new CharacterAdapter(this);
        vertical_recycler_view.setAdapter(characterAdapter);
    }

    @Override
    public void onLocationSelected(Location location) {
        // Seçilen lokasyona ait karakterler listeye eklendi.
        characterAdapter.filterCharacters(location);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetLocationsTask extends AsyncTask<Void, Void, List<Location>>  {

        @Override
        protected List<Location> doInBackground(Void... voids) {
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
        protected void onPostExecute(List<Location> locationNames) {
            super.onPostExecute(locationNames);
            if (locationNames == null) {
                locationNames = new ArrayList<>(); // Null yerine boş bir liste atıyoruz.
            }
            locationAdapter.setLocations((ArrayList<Location>) locationNames);
            String locationUrl = "https://rickandmortyapi.com/api/character";

            new GetCharactersTask(locationUrl).execute();
        }

        private List<Location> getLocationNamesFromResponse(String response) {
            List<Location> locations = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length() && i < 20; i++) {
                    Location locationInstance = new Location();
                    locationInstance.residentIds = new ArrayList<>();
                    JSONObject locationObject = jsonArray.getJSONObject(i);
                    locationInstance.locationName = locationObject.getString("name");
                    JSONArray residentUrls = locationObject.getJSONArray("residents");
                    for (int j = 0; j < residentUrls.length(); j++) {
                        try {
                            locationInstance.residentIds.add(Integer.parseInt(residentUrls.getString(j).split("character/")[1]));
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                    locations.add(locationInstance);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (Location l:locations) {
                System.out.print(l.locationName);
                System.out.print("-");
                for (Integer i:l.residentIds ) {
                    System.out.print(i);
                }
                System.out.println();
            }

            return locations;
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
    }
}