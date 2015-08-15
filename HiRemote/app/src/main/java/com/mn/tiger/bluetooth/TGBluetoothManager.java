package com.mn.tiger.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.mn.tiger.app.TGApplication;

import java.util.HashMap;
import java.util.List;

/**
 * Created by peng on 15/8/2.
 */
public class TGBluetoothManager
{
    public static final int REQUEST_CODE_ENABLE_BT = 1001;

    private volatile static TGBluetoothManager instance;

    private TGBLEScanParameter scanParameter;

    private OnScanPeripheralListener onScanPeripheralListener;

    private BluetoothAdapter bluetoothAdapter;

    public static TGBluetoothManager getInstance()
    {
        if(null == instance)
        {
            synchronized (TGBluetoothManager.class)
            {
                if(null == instance)
                {
                    instance = new TGBluetoothManager();
                }
            }
        }
        return instance;
    }

    public static boolean isSupportBluetoochLowEnergy()
    {
        if(TGApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            return true;
        }

        return false;
    }

    private TGBluetoothManager()
    {
        bluetoothAdapter = ((BluetoothManager)TGApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }

    public void scan(OnScanPeripheralListener onScanPeripheralListener)
    {
        this.onScanPeripheralListener = onScanPeripheralListener;
    }

    public void stopScan()
    {
        if(bluetoothAdapter.isDiscovering())
        {

        }
    }

    public void setScanServicesAndCharacteristics(TGBLEScanParameter parameter)
    {

    }

    public void turnOnBluetooch(Activity activity)
    {
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BT);
        }
    }

    public void turnOnBluetoochInBackground()
    {
        bluetoothAdapter.enable();
    }

    public void turnOffBluetoochInBackground()
    {
        bluetoothAdapter.disable();
    }

    public static interface OnScanPeripheralListener
    {
        void onFindPeripheral();

    }

    public void onActivityResult(int requestCode, int result, Intent data)
    {

    }


    public static enum ConnectState
    {
        Connected,
        Nonsupport,
        BluetoothOff,
        Disconnect
    }
}
