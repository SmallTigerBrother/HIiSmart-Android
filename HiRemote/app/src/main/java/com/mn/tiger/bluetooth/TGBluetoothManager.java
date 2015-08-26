package com.mn.tiger.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;
import com.mn.tiger.log.Logger;

import java.util.List;

/**
 * Created by peng on 15/8/2.
 */
public class TGBluetoothManager implements BluetoothAdapter.LeScanCallback
{
    private static final Logger LOG = Logger.getLogger(TGBluetoothManager.class);

    public static final String ACTION_BLE_STATE_CHANGE = "ble_state_change";

    public static final int BLE_STATE_CONNECTED = 1;

    public static final int BLE_STATE_DISCONNECTED = 2;

    public static final int BLE_STATE_NONSUPPORT = 3;

    public static final int BLE_STATE_POWEROFF = 4;

    private static final String PERIPHERAL_INFO_KEY = "peripheralInfo";

    private static final String BLE_STATE_KEY = "ble_state";

    public static final int REQUEST_CODE_ENABLE_BT = 1001;

    public static final int SCAN_TIME_OUT = 30000;

    private TGBLEScanParameter scanParameter;

    private BluetoothAdapter bluetoothAdapter;

    private TGBLEPeripheralInfo currentPeripheral;

    private TGBLEPeripheralInfo lastPeripheral;

    private boolean isScanning = false;

    private String targetPeripheral = null;

    private static Handler handler = new Handler();

    public static boolean isSupportBluetoothLowEnergy()
    {
        if(TGApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            return true;
        }

        sendBroadcast(BLE_STATE_NONSUPPORT, null);
        return false;
    }

    protected TGBluetoothManager()
    {
        if(isSupportBluetoothLowEnergy())
        {
            bluetoothAdapter = ((BluetoothManager)TGApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        }
    }

    public void scan()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                stopScan();
            }
        }, SCAN_TIME_OUT);

        this.lastPeripheral = currentPeripheral;
        this.currentPeripheral = null;
        this.isScanning = true;

        if(null != bluetoothAdapter)
        {
            if(null != scanParameter)
            {
                bluetoothAdapter.startLeScan(scanParameter.getServiceUUIDs(), this);
            }
            else
            {
                bluetoothAdapter.startLeScan(this);
            }
        }
    }

    public void stopScan()
    {
        this.targetPeripheral = null;
        this.isScanning = false;
        if(null != bluetoothAdapter)
        {
            bluetoothAdapter.stopLeScan(this);
        }
    }

    public boolean isScanning()
    {
        return isScanning;
    }

    /**
     * 扫描并连接指定UUID的设备
     * @param peripheralName
     */
    public void scanAndConnect2Peripheral(String peripheralName)
    {
        this.targetPeripheral = peripheralName;
    }

    public void setScanParameter(TGBLEScanParameter parameter)
    {
        this.scanParameter = parameter;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        if(TextUtils.isEmpty(targetPeripheral))
        {
            onDiscoveredTargetDevice(device);
        }
        else
        {
            if(targetPeripheral.equals(device.getName()))
            {
                onDiscoveredTargetDevice(device);
            }
        }
    }

    private synchronized void onDiscoveredTargetDevice(BluetoothDevice device)
    {
        if(null == currentPeripheral)
        {
            BluetoothGatt bluetoothGatt = device.connectGatt(TGApplication.getInstance(), false,
                    bluetoothGattCallback);
            currentPeripheral = new TGBLEPeripheralInfo();
            currentPeripheral.setPeripheralName(device.getName());
        }
        stopScan();
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

    public TGBLEPeripheralInfo getCurrentPeripheral()
    {
        return currentPeripheral;
    }

    public TGBLEPeripheralInfo getLastPeripheral()
    {
        return lastPeripheral;
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            LOG.d("[Method:onServicesDiscovered] status == " + status);
            if(status == BluetoothGatt.GATT_SUCCESS)
            {
                List<BluetoothGattService> bleServiceList = gatt.getServices();
                for (BluetoothGattService bleGattService: bleServiceList)
                {
                    List<BluetoothGattCharacteristic> characteristics = bleGattService.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics)
                    {
                        LOG.d("[Method:onServicesDiscovered] characteristicUUID  == " + characteristic.getUuid());
                        gatt.readCharacteristic(characteristic);
                    }
                }
            }

//            BluetoothGattService gattService = gatt.getService(UUID.fromString(""));
//
//            BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(UUID.fromString(""));
//
//            gatt.setCharacteristicNotification(gattCharacteristic, true);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
            //监听characteristic的值变化
            String value = new String(characteristic.getValue());
            LOG.d("[Method:onCharacteristicChanged]  characteristic == " + characteristic.getUuid() +
                    " value == " + value);
        }

        @Override
        public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            LOG.d("[Method:onCharacteristicRead] status == " + status + "  characteristic == " + characteristic.getUuid());
            String value = new String(characteristic.getValue());
            LOG.d("[Method:onCharacteristicRead] value == " + value);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            LOG.d("[Method:onCharacteristicWrite] status == " + status + "  characteristic == " + characteristic.getUuid());
            String value = new String(characteristic.getValue());
            LOG.d("[Method:onCharacteristicWrite] value == " + value);
        }

        @Override
        public synchronized void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            switch (newState)
            {
                case BluetoothProfile.STATE_CONNECTED:
                    gatt.discoverServices();
                    sendBroadcast(BLE_STATE_CONNECTED, currentPeripheral);
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    sendBroadcast(BLE_STATE_DISCONNECTED, currentPeripheral);
                    break;
            }
        }

    };

    private synchronized static void sendBroadcast(int bleState, TGBLEPeripheralInfo peripheralInfo)
    {
        if(null != peripheralInfo)
        {
            Intent intent = new Intent();
            intent.setAction(ACTION_BLE_STATE_CHANGE);
            intent.putExtra(BLE_STATE_KEY,bleState);
            intent.putExtra(PERIPHERAL_INFO_KEY, peripheralInfo);
            TGApplication.getInstance().sendBroadcast(intent);
        }
    }

    public static TGBLEPeripheralInfo getBLEPeripheralInfo(Intent data)
    {
        return (TGBLEPeripheralInfo)data.getSerializableExtra(PERIPHERAL_INFO_KEY);
    }

    public static int getBLEState(Intent data)
    {
        return data.getIntExtra(BLE_STATE_KEY, BLE_STATE_DISCONNECTED);
    }
}
