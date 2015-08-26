package com.mn.tiger.bluetooth.event;

import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;

/**
 * Created by peng on 15/8/15.
 */
public class ConnectPeripheralEvent
{
    private TGBLEPeripheralInfo peripheralInfo;

    public ConnectPeripheralEvent()
    {
    }

    public void setPeripheralInfo(TGBLEPeripheralInfo peripheralInfo)
    {
        this.peripheralInfo = peripheralInfo;
    }

    public TGBLEPeripheralInfo getPeripheralInfo()
    {
        return peripheralInfo;
    }

}
