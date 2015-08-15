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
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by peng on 15/8/2.
 */
public class TGBluetoothManager
{
    public static final int REQUEST_CODE_ENABLE_BT = 1001;

    private TGBLEScanParameter scanParameter;

    private OnScanPeripheralListener onScanPeripheralListener;

    private BluetoothAdapter bluetoothAdapter;

    private TGBLEPeripheralInfo currentPeripheral;

    private TGBLEPeripheralInfo lastPeripheral;

    public static boolean isSupportBluetoothLowEnergy()
    {
        if(TGApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            return true;
        }

        return false;
    }

    protected TGBluetoothManager()
    {
        bluetoothAdapter = ((BluetoothManager)TGApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }

    public void scan(OnScanPeripheralListener onScanPeripheralListener)
    {
        this.onScanPeripheralListener = onScanPeripheralListener;
        this.lastPeripheral = currentPeripheral;
        this.currentPeripheral = null;
    }

    public void stopScan()
    {
        if(bluetoothAdapter.isDiscovering())
        {

        }
    }

    /**
     * 扫描并连接指定UUID的设备
     * @param uuid
     */
    public void scanAndConnect2Peripheral(String uuid)
    {

    }

    public void setScanServicesAndCharacteristics(TGBLEScanParameter parameter)
    {

    }

    public void turnOnBluetooth(Activity activity)
    {
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BT);
        }
    }

    public void turnOnBluetoothInBackground()
    {
        bluetoothAdapter.enable();
    }

    public void turnOffBluetoothInBackground()
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

    public TGBLEPeripheralInfo getCurrentPeripheral()
    {
        return currentPeripheral;
    }

    public TGBLEPeripheralInfo getLastPeripheral()
    {
        return lastPeripheral;
    }

    public static enum ConnectState
    {
        Connected,
        Nonsupport,
        BluetoothOff,
        Disconnect
    }
}
