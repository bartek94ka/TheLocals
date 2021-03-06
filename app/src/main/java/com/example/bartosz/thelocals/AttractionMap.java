package com.example.bartosz.thelocals;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.view.MenuItem;


import com.example.bartosz.thelocals.GPSTrackers.GPSTracker;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Providers.AttractionInfoProvider;
import com.example.bartosz.thelocals.Providers.AttractionMarkerProvider;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bolts.Continuation;

public class AttractionMap extends Fragment implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private OnFragmentInteractionListener mListener;
    private GoogleMap map;
    private MapView mapView;
    private View view;
    private Location location;

    private GPSTracker gpsTracker;
    private GoogleMapProvider googleMapProvider;
    private AttractionInfoProvider attractionInfoProvider;
    private Attraction attraction;
    private AttractionMarkerProvider attractionMarkerProvider;
    private double latitude;
    private double longitude;
    private String attractionId;

    public AttractionMap() {
        // Required empty public constructor
        attractionId = "-LHXEtKMyp-2BuNO5F19";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = new OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Uri uri) {

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attraction_map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        googleMapProvider = new GoogleMapProvider();
        attractionInfoProvider = new AttractionInfoProvider("Wielkopolskie");
        attractionMarkerProvider = new AttractionMarkerProvider();
        //ArrayList<Attraction> list = attractionInfoProvider.GetAllAttractions().getResult();
        SetAttractionDataById(attractionId);
        gpsTracker = new GPSTracker(getContext());
        location = gpsTracker.getLocation();
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

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

    private void SetCameraOnAttraction(){
        /*
        attractionInfoProvider.GetAttractionById(attractionId).
                continueWith(new Continuation<Attraction, Attraction>() {
                    @Override
                    public Attraction then(bolts.Task<Attraction> task) throws Exception {
                        attraction = task.getResult();
                        return attraction;
                    }
                });
        */
        if(attraction != null){
            map.clear();
            Double latitude = Double.parseDouble(attraction.Latitude);
            Double longitude = Double.parseDouble(attraction.Longitude);
            LatLng myLocation = new LatLng(latitude, longitude);
            Circle circle = map.addCircle(new CircleOptions().center(myLocation).radius(1000).strokeColor(Color.RED));
            circle.setVisible(false);
            int zoom = googleMapProvider.GetZoomLevel(circle);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom - 1));
            map.addMarker(attractionMarkerProvider.GetMarker(attraction));
        }
    }

    private void SetAttractionDataById(String attractionId){
        attractionInfoProvider.GetAttractionById(attractionId).
                continueWith(new Continuation<Attraction, Attraction>() {
                    @Override
                    public Attraction then(bolts.Task<Attraction> task) throws Exception {
                        attraction = task.getResult();
                        SetCameraOnAttraction();
                        return attraction;
                    }
                });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        //UpdateUserLocationOnMap();
        SetCameraOnAttraction();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
