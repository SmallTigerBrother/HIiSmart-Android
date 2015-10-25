package com.mn.tiger.map;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mn.tiger.location.GoogleLocationManager;
import com.mn.tiger.location.ILocationManager;
import com.mn.tiger.location.TGLocation;
import com.mn.tiger.log.Logger;

/**
 * Created by Dalang on 2015/8/23.
 */
public class GoogleMapManager implements IMapManager, LocationSource, LocationListener
{
    private static final Logger LOG = Logger.getLogger(GoogleMapManager.class);

    private Activity activity;

    private MapView mapView;

    private GoogleMap googleMap;

    private GoogleLocationManager locationManager;

    private OnLocationChangedListener onLocationChangedListener;

    public GoogleMapManager(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void init(ViewGroup mapContainer, Bundle savedInstanceState)
    {
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType( GoogleMap.MAP_TYPE_NORMAL );
        mapView = new MapView(activity, options);
        mapView.onCreate(savedInstanceState);
        mapContainer.addView(mapView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        googleMap = mapView.getMap();
        setUpMap();
    }

    /**
     * 初始化地图
     */
    private void setUpMap()
    {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setLocationSource(this);
    }

    @Override
    public void centerTo(double latitude, double longitude)
    {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17);
        googleMap.moveCamera(cameraUpdate);
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
        markerOptions.draggable(false);
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.title(title);

        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    @Override
    public void showMyLocation()
    {
        googleMap.getMyLocation();
    }

    @Override
    public void clear()
    {
        googleMap.clear();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        LOG.d("[Method:LocationSource:onLocationChanged] lat == " + location.getLatitude() + " ; long == " + location.getLongitude());
        if(null != onLocationChangedListener)
        {
            onLocationChangedListener.onLocationChanged(location);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        LOG.d("[Method:LocationSource:activate] ");
        this.onLocationChangedListener = onLocationChangedListener;
        if(null != this.onLocationChangedListener)
        {
            locationManager = new GoogleLocationManager();
            locationManager.setLocationListener(new ILocationManager.ILocationListener()
            {
                @Override
                public void onReceiveLocation(TGLocation location)
                {
                    onLocationChanged(location.getLocation());
                }
            });
            locationManager.requestLocationUpdates();
        }
    }

    @Override
    public void deactivate()
    {
        LOG.d("[Method:LocationSource:deactivate]");
        onLocationChangedListener = null;
        if(null != locationManager)
        {
            locationManager.removeLocationUpdates();
        }
        locationManager = null;
    }
}
