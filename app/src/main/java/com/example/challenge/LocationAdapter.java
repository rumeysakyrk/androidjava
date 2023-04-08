package com.example.challenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private ArrayList<String> locationNames = new ArrayList<>();
    private Context context;
    private String selectedLocation;


    public interface LocationAdapterListener {
        void onLocationSelected(String location);
    }

    private LocationAdapterListener listener;

    public LocationAdapter(Context context, LocationAdapterListener listener) {
        this.context = context;
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        this.listener = listener;
    }


    public void setLocationNames(ArrayList<String> locationNames) {
        if (locationNames != null) {
            this.locationNames = locationNames;
            notifyDataSetChanged();
        }
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }


    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        LocationViewHolder viewHolder = new LocationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        String name = locationNames.get(position);
        holder.locationButton.setText(name);

        holder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLocation = locationNames.get(holder.getAdapterPosition());
                setSelectedLocation(selectedLocation);
                listener.onLocationSelected(selectedLocation);
            }
        });

    }


    @Override
    public int getItemCount() {
        return locationNames.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public Button locationButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationButton = itemView.findViewById(R.id.location_button);
        }
    }
}