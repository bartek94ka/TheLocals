package com.example.bartosz.thelocals;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bartosz.thelocals.GPSTrackers.GPSTracker;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Providers.AttractionInfoProvider;
import com.example.bartosz.thelocals.Providers.AttractionMarkerProvider;
import com.example.bartosz.thelocals.Providers.GoogleMapProvider;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class SelectedAttractionsOnMap extends Fragment implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private AttractionMap.OnFragmentInteractionListener mListener;
    private GoogleMap map;
    private MapView mapView;
    private View view;
    private Location location;

    private GPSTracker gpsTracker;
    private GoogleMapProvider googleMapProvider;
    private AttractionInfoProvider attractionInfoProvider;
    private ArrayList<Attraction> attractions;
    private AttractionMarkerProvider attractionMarkerProvider;

    public SelectedAttractionsOnMap(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = new AttractionMap.OnFragmentInteractionListener() {
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
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_selected_attractions_map));
        SetAttractionListFromArguments();
        googleMapProvider = new GoogleMapProvider();
        //attractionInfoProvider = new AttractionInfoProvider("Wielkopolskie");
        //attractionInfoProvider = new AttractionInfoProvider("Kujawsko-pomorskie");
        attractionMarkerProvider = new AttractionMarkerProvider();
        //ArrayList<Attraction> list = attractionInfoProvider.GetAllAttractions().getResult();
        //SetAttractionListRegionName("region name");
        gpsTracker = new GPSTracker(getContext());
        location = gpsTracker.getLocation();
        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    private void SetAttractionListFromArguments(){
        Bundle args = getArguments();
        attractions = new ArrayList<Attraction>();
        if(args != null){
            ArrayList<Attraction> attractionList = (ArrayList<Attraction>)args.getSerializable("attractions");
            if(attractionList != null){
                attractions.addAll(attractionList);
            }
        }
    }

    private void SetCameraOnAttractions(){

        map.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (final Attraction attraction: attractions) {

            MarkerOptions marker = attractionMarkerProvider.GetMarker(attraction);
            builder.include(marker.getPosition());
            map.addMarker(marker);
        }
        LatLngBounds bounds = builder.build();
        int padding = 40;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cameraUpdate);
    }

    private void SetAttractionListRegionName(String provinceName){
        attractionInfoProvider.GetAllAttractionsByProvince(provinceName).
                continueWith(new Continuation<ArrayList<Attraction>, ArrayList<Attraction>>() {
                    @Override
                    public ArrayList<Attraction> then(bolts.Task<ArrayList<Attraction>> task) throws Exception {
                        attractions = task.getResult();
                        SetCameraOnAttractions();
                        //return new ArrayList<Attraction>();
                        //return attraction;
                        return attractions;
                    }
                }).onSuccess(new Continuation<ArrayList<Attraction>, Void>() {

            @Override
            public Void then(Task<ArrayList<Attraction>> task) throws Exception {

                return null;
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        SetCameraOnAttractions();
        //SetAttractionListRegionName(provinceName);
        //UpdateUserLocationOnMap();
        //SetCameraOnAttraction();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
