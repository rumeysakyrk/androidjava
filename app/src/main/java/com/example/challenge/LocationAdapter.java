package com.example.challenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private ArrayList<Location> locations = new ArrayList<>();
    private Context context;
    private Location selectedLocation;
    private int selectedPosition = 0;

    public interface LocationAdapterListener {
        void onLocationSelected(Location location);
    }

    private LocationAdapterListener listener;

    public LocationAdapter(Context context, LocationAdapterListener listener) {
        this.context = context;
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        this.listener = listener;
    }


    public void setLocations(ArrayList<Location> locations) {
        if (locations != null) {
            this.locations = locations;
            notifyDataSetChanged();
        }
    }

    public Location getSelectedLocation() {
        System.out.println(listener);
        return selectedLocation;
    }


    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        LocationViewHolder viewHolder = new LocationViewHolder(view);

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.setMargins(5, 0, 5, (int) (16 * context.getResources().getDisplayMetrics().density));
        view.setLayoutParams(lp);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        String name = locations.get(position).locationName;
        holder.locationButton.setText(name);


        // Set the background color of the button based on its position
        if (selectedPosition == position) {
            holder.locationButton.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200));
        } else {
            holder.locationButton.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700));
        }

        holder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location selectedLocation = locations.get(holder.getAdapterPosition());
                setSelectedLocation(selectedLocation);
                listener.onLocationSelected(selectedLocation);
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                if (previousSelectedPosition != -1) {
                    notifyItemChanged(previousSelectedPosition);
                }
                notifyItemChanged(selectedPosition);
            }
        });
    }



    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public Button locationButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationButton = itemView.findViewById(R.id.location_button);
        }
    }
}