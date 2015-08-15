package com.lepow.hiremote.bluetooth;

import com.mn.tiger.bluetooth.TGBluetoothManager;
import com.mn.tiger.bluetooth.event.ConnectPeripheralEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by peng on 15/8/15.
 */
public class ScanNewPeripheralActivity extends ScanPeripheralActivity
{
    @Subscribe
    public void onConnectDevice(final ConnectPeripheralEvent event)
    {
        if(event.getState() == TGBluetoothManager.ConnectState.Disconnect)
        {
            HSBLEPeripheralManager.getInstance().showNotFoundNewDeviceDialog(this);
            return;
        }

        super.onConnectDevice(event);
    }

}
