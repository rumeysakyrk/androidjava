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


        vertical_recycler_view = findViewById(R.id.vertical_recycler_view);

        characterAdapter = new CharacterAdapter(this);
        vertical_recycler_view.setAdapter(characterAdapter);


    }

    @Override
    public void onLocationSelected(Location location) {
        // Seçilen lokasyona ait karakterler listeye eklendi.
        characterAdapter.filterCharacters(location);
    }



}