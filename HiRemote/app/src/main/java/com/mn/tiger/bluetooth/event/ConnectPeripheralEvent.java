package com.mn.tiger.bluetooth.event;

import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.mn.tiger.bluetooth.TGBluetoothManager.ConnectState;

/**
 * Created by peng on 15/8/15.
 */
public class ConnectPeripheralEvent
{
    private ConnectState state = ConnectState.Disconnect;

    private PeripheralInfo peripheralInfo;

    public ConnectPeripheralEvent()
    {
        this.state = state;
    }

    public void setPeripheralInfo(PeripheralInfo peripheralInfo)
    {
        this.peripheralInfo = peripheralInfo;
    }

    public PeripheralInfo getPeripheralInfo()
    {
        return peripheralInfo;
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
