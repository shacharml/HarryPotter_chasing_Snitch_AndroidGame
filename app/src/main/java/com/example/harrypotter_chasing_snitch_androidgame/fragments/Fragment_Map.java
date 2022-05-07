package com.example.harrypotter_chasing_snitch_androidgame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.harrypotter_chasing_snitch_androidgame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    //Binyamina lat/lng
    private double lat = 32.517078;
    private double lag = 34.955096;
    private String titleToMap = "Binyamina";


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = ((SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.fragment_map_google));
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(lat, lag);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(titleToMap));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setMinZoomPreference(7);
    }

    public void getLocation(double lat, double lng, String titleToMap) {
        this.lat = lat;
        this.lag = lng;
        this.titleToMap = titleToMap;
        onMapReady(mMap);
    }

}
