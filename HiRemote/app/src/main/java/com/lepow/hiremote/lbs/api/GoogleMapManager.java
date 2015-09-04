package com.lepow.hiremote.lbs.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Dalang on 2015/8/23.
 */
public class GoogleMapManager implements IMapManager
{
    private Activity activity;

    private MapView mapView;

    private GoogleMap googleMap;

    public GoogleMapManager(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void init(ViewGroup mapContainer, Bundle savedInstanceState)
    {
        mapView = new MapView(activity);
        mapView.onCreate(savedInstanceState);
        mapContainer.addView(mapView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        googleMap = mapView.getMap();
    }

    @Override
    public void centerTo(double latitude, double longitude)
    {

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy()
    {
        mapView.onDestroy();
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        mapView.onPause();
    }

    @Override
    public void addMarker(double latitude, double longitude, String title)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.title(title);

        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }
}
