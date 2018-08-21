package com.example.bartosz.thelocals.Providers;

import com.example.bartosz.thelocals.Models.Attraction;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AttractionMarkerProvider {

    public MarkerOptions GetMarker(Attraction attraction){
        MarkerOptions marker = null;
        if(attraction != null){
            Double latitude = Double.parseDouble(attraction.Latitude);
            Double longitude = Double.parseDouble(attraction.Longitude);
            LatLng position = new LatLng(latitude, longitude);
            marker = new MarkerOptions().position(position).
                    title(attraction.Name);
        }
        return marker;
    }
}
