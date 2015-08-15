package com.mn.tiger.bluetooth.event;

import com.lepow.hiremote.connect.data.DeviceInfo;
import com.mn.tiger.bluetooth.TGBluetoothManager.ConnectState;

/**
 * Created by peng on 15/8/15.
 */
public class Connect2DeviceEvent
{
    private ConnectState state = ConnectState.Unknown;

    private DeviceInfo deviceInfo;

    public Connect2DeviceEvent()
    {
        this.state = state;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo)
    {
        this.deviceInfo = deviceInfo;
    }

    public DeviceInfo getDeviceInfo()
    {
        return deviceInfo;
    }

    public void setState(ConnectState state)
    {
        this.state = state;
    }

    public ConnectState getState()
    {
        return state;
    }

}
