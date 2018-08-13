package com.example.bartosz.thelocals.Providers;

import com.google.android.gms.maps.model.Circle;

public class GoogleMapProvider {

    public int GetZoomLevel(Circle circle){
        int zoomLevel = 1;
        if(circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }
}
