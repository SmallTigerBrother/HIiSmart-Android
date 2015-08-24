package com.lepow.hiremote.lbs.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

/**
 * Created by Dalang on 2015/8/23.
 */
public class AMapManager implements  IMapManager
{
    private MapView mapView;
    private AMap aMap;

    private Activity activity;

    public AMapManager(Activity activity)
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

        aMap = mapView.getMap();
        setUpMap();
    }

    private void setUpMap()
    {
        //TODO 初始化地图
    }

    public void addMarker(double latitude, double langitude, String title)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(false);
        markerOptions.position(new LatLng(latitude, langitude));
        markerOptions.title(title);
        Marker marker = aMap.addMarker(markerOptions);

        marker.showInfoWindow();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        mapView.onSaveInstanceState(outState);
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
    public void onDestroy()
    {
        mapView.onDestroy();
    }
}
