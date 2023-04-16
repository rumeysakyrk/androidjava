package com.example.challenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private ArrayList<Location> locations = new ArrayList<>();
    private Context context;
    private Location selectedLocation;
    private int selectedPosition = 0;
    private LocationAdapterListener listener;
    private int currentPage = 1;
    private int totalPages = 1;
    private ProgressBar progressBar;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void loadNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            new GetLocationsTask().execute(currentPage);
        }

    }
    public interface LocationAdapterListener {
        void onLocationSelected(Location location);

    }

    public LocationAdapter(Context context, LocationAdapterListener listener, ProgressBar progressBar) {
        this.context = context;
        this.progressBar=progressBar;
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        this.listener = listener;
        new GetLocationsTask().execute(currentPage);
    }
    public void setLocations(ArrayList<Location> locations) {
        if (locations != null) {
            this.locations = locations;
            notifyDataSetChanged();
        }
    }
    public Location getSelectedLocation() {
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
        if (position == locations.size() - 1) {
            // RecyclerView'nin sonuna ulaşıldığında, yeni sayfayı yükle
            currentPage++;
            new GetLocationsTask().execute(currentPage);
        }
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

    @SuppressLint("StaticFieldLeak")
    private class GetLocationsTask extends AsyncTask<Integer, Void, List<Location>> {

        @Override
        protected List<Location> doInBackground(Integer... integers) {
            HttpURLConnection conn = null;
            int pageNumber = integers[0];
            try {
                URL url = new URL("https://rickandmortyapi.com/api/location?page=" + pageNumber);
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
                locationNames = new ArrayList<>();
            }
            locations.addAll(locationNames);
            notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);
            if (currentPage == totalPages) {
                progressBar.setVisibility(View.GONE);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        progressBar.setVisibility(View.GONE);

                }
            }, 1000);

            for (Location location : locationNames) {
                if (location.locationName.equals("Earth (C-137)")) {
                    listener.onLocationSelected(location);
                    break;
                }
            }
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
            return locations;
        }
    }
}