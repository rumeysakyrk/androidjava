package com.example.challenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private List<Character> characters;
    private List<Character> characters_org;
    private Context context;

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

    public void filterCharacters(String location) {
        if(characters_org == null){
            characters_org=characters;
        }
        List<Character> filteredCharacters = new ArrayList<>();
        for (Character character : characters_org) {
            if (character.getLocation().equals(location)) {
                filteredCharacters.add(character);
            }
        }
        characters = filteredCharacters;
        notifyDataSetChanged();
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


    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView genderTextView;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.character_image);
            nameTextView = itemView.findViewById(R.id.character_name);
            genderTextView = itemView.findViewById(R.id.character_gender);
        }
    }
}