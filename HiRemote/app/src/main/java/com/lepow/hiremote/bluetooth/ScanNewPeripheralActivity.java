package com.lepow.hiremote.bluetooth;

/**
 * Created by peng on 15/8/15.
 */
public class ScanNewPeripheralActivity extends ScanPeripheralActivity
{
    @Override
    protected void scanDevice()
    {
        HSBLEPeripheralManager.getInstance().scanNewPeripheral();
    }

    @Override
    protected void onScanRetry()
    {
        super.scanDevice();
    }
}
