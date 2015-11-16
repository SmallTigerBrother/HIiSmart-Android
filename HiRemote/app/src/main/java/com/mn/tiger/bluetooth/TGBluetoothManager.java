package com.mn.tiger.bluetooth;

import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.mn.tiger.app.TGApplicationProxy;

/**
 * Created by Dalang on 2015/8/30.
 */
public class TGBluetoothManager
{
    public static boolean isBlueToothHeadsetConnected()
    {
        int state = ((BluetoothManager) TGApplicationProxy.getInstance().getApplication().getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter().getProfileConnectionState(android.bluetooth.BluetoothProfile.HEADSET);
        return state == BluetoothProfile.STATE_CONNECTED;
    }
}
