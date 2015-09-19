package com.lepow.hiremote.lbs.api;

import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by Dalang on 2015/8/23.
 */
public interface IMapManager
{
    void init(ViewGroup mapContainer, Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);

    void onDestroy();

    void onResume();

    void onPause();

    void addMarker(double latitude, double langitude, String title);

    void centerTo(double latitude, double longitude);

    void showMyLocation();

    void clear();
}
