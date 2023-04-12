package com.example.challenge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class DetailPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Intent'ten karakter detaylarını alma
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String gender = intent.getStringExtra("gender");
        String imageUrl = intent.getStringExtra("imageUrl");
        String status= intent.getStringExtra("status");
        String species=intent.getStringExtra("species");
        String origin =intent.getStringExtra("origin");
        String episodes=intent.getStringExtra("episode");
        String location=intent.getStringExtra("location");
        String created=intent.getStringExtra("created");

        // Detay sayfasındaki ImageView ve TextView öğelerine karakter detaylarını yerleştirme
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = findViewById(R.id.character_image);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView nameTextView = findViewById(R.id.character_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView genderTextView = findViewById(R.id.character_gender);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView originTextView = findViewById(R.id.character_origin);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView speciesTextView = findViewById(R.id.character_species);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView statusTextView = findViewById(R.id.character_status);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView episodesTextView = findViewById(R.id.episodes);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView locationTextView = findViewById(R.id.location);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView createdTextView = findViewById(R.id.createdAt);

        nameTextView.setText(name);
        genderTextView.setText(gender);
        originTextView.setText(origin);
        statusTextView.setText(status);
        speciesTextView.setText(species);
        episodesTextView.setText(episodes);
        locationTextView.setText(location);
        createdTextView.setText(created);
        Picasso.get().load(imageUrl).into(imageView);
        getSupportActionBar().setTitle(name);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}