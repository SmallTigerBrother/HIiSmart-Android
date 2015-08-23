package com.lepow.hiremote.lbs.api;

import android.os.Bundle;

/**
 * Created by Dalang on 2015/8/23.
 */
public interface IMapManager
{
    void init(Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);

    void onDestroy();

    void onResume();

    void onPause();

    void addMarker(double latitude, double langitude, String title);
}
