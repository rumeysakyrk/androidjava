package com.example.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        // Intent'ten karakter detaylarını alma
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String gender = intent.getStringExtra("gender");
        String imageUrl = intent.getStringExtra("imageUrl");
        String status= intent.getStringExtra("status");
        String species=intent.getStringExtra("species");
        String origin =intent.getStringExtra("origin");

        // Detay sayfasındaki ImageView ve TextView öğelerine karakter detaylarını yerleştirme
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = findViewById(R.id.character_image);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView nameTextView = findViewById(R.id.character_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView genderTextView = findViewById(R.id.character_gender);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView originTextView = findViewById(R.id.character_origin);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView speciesTextView = findViewById(R.id.character_species);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView statusTextView = findViewById(R.id.character_status);

        nameTextView.setText(name);
        genderTextView.setText(gender);
        originTextView.setText(origin);
        statusTextView.setText(status);
        speciesTextView.setText(species);
        Picasso.get().load(imageUrl).into(imageView);
    }

}