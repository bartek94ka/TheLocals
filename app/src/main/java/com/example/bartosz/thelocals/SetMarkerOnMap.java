package com.example.bartosz.thelocals;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bartosz.thelocals.GPSTrackers.GPSTracker;
import com.example.bartosz.thelocals.Providers.GoogleMapProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetMarkerOnMap extends Fragment implements OnMapReadyCallback {

    private View view;
    private MapView mapView;
    private GoogleMap map;
    private Location location;
    private MarkerOptions markerOptions;

    private GoogleMapProvider googleMapProvider;
    private GPSTracker gpsTracker;

    private double latitude;
    private double longitude;

    public SetMarkerOnMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_set_marker_on_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        InitializeVeribles();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        UpdateUserLocationOnMap();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               map.clear();
               markerOptions = new MarkerOptions().position(latLng);
               map.addMarker(markerOptions);

            }
        });
    }

    public MarkerOptions GetMarkerOption(){
        return markerOptions;
    }

    private void InitializeVeribles(){
        googleMapProvider = new GoogleMapProvider();
        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        gpsTracker = new GPSTracker(getContext());
        location = gpsTracker.getLocation();
        SetUserCurrentLocation();
    }

    private void UpdateUserLocationOnMap()
    {
        map.clear();
        LatLng myLocation = new LatLng(latitude, longitude);
        Circle circle = map.addCircle(new CircleOptions().center(myLocation).radius(10000).strokeColor(Color.RED));
        circle.setVisible(false);
        int zoom = googleMapProvider.GetZoomLevel(circle);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom - 1));
    }

    private void SetUserCurrentLocation(){
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
}
