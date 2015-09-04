package com.mn.tiger.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;
import com.mn.tiger.log.Logger;

import java.util.UUID;

/**
 * Created by peng on 15/8/2.
 * BLE管理类
 */
public class TGBLEManager implements BluetoothAdapter.LeScanCallback
{
    private static final Logger LOG = Logger.getLogger(TGBLEManager.class);

    /**
     * BLE连接状态改变Action
     */
    public static final String ACTION_BLE_STATE_CHANGE = "ble_state_change";

    /**
     * BLE状态——已连接
     */
    public static final int BLE_STATE_CONNECTED = 1;

    /**
     * BLE状态——断开连接
     */
    public static final int BLE_STATE_DISCONNECTED = 2;

    /**
     * BLE状态——不支持4.0
     */
    public static final int BLE_STATE_NONSUPPORT = 3;

    /**
     * BLE状态——关闭
     */
    public static final int BLE_STATE_POWER_OFF = 4;

    /**
     * BLE状态——打开
     */
    public static final int BLE_STATE_POWER_ON = 5;

    /**
     * 保存蓝牙设备信息的键值
     */
    private static final String PERIPHERAL_INFO_KEY = "peripheralInfo";

    /**
     * 保存BLE状态的键值
     */
    private static final String BLE_STATE_KEY = "ble_state";

    /**
     * 请求打开蓝牙的requestCode
     */
    public static final int REQUEST_CODE_ENABLE_BLUETOOTH = 1001;

    /**
     * 扫描超时时间
     */
    public static final int SCAN_TIME_OUT = 30000;


    private TGBLEScanParameter scanParameter;

    /**
     * 蓝牙适配器
     */
    private BluetoothAdapter bluetoothAdapter;

    /**
     * 当前连接的设备
     */
    private TGBLEPeripheralInfo currentPeripheral;

    /**
     * 上次连接的蓝牙设备
     */
    private TGBLEPeripheralInfo lastPeripheral;

    private BluetoothGatt currentGatt;

    /**
     * 是否正在扫描
     */
    private boolean isScanning = false;

    /**
     * 指定要连接的设备信息
     */
    private String targetPeripheral = null;

    /**
     * 停止扫描Message的what值
     */
    private static final int MESSAGE_STOP_SCAN = 0x0001;

    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            TGBLEManager.this.handleMessage(msg);
        }
    };

    /**
     * 接受handler传递过来的消息
     * @param msg
     */
    protected void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case MESSAGE_STOP_SCAN:
                stopScan();
                break;
            default:
                break;
        }
    }

    /**
     * 判断您是否支持蓝牙4.0
     * @return
     */
    public static boolean isSupportBluetoothLowEnergy()
    {
        if(TGApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            return true;
        }

        sendBroadcast(BLE_STATE_NONSUPPORT, null);
        return false;
    }

    protected TGBLEManager()
    {
        if(isSupportBluetoothLowEnergy())
        {
            bluetoothAdapter = ((BluetoothManager)TGApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            //注册广播监听蓝牙关闭、打开事件
            TGApplication.getInstance().registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if(state != BluetoothAdapter.STATE_ON)
                    {
                        currentPeripheral = null;
                        currentGatt = null;
                        isScanning = false;
                        sendBroadcast(BLE_STATE_POWER_OFF, null);
                    }
                    else
                    {
                        sendBroadcast(BLE_STATE_POWER_ON, null);
                        scan();
                    }
                }
            }, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
    }

    /**
     * 扫描蓝牙4.0设备
     */
    public void scan()
    {
        LOG.d("[Method:scan]");
        if(!bluetoothAdapter.isEnabled())
        {
            sendBroadcast(BLE_STATE_POWER_OFF, null);
            return;
        }

        if(null != currentPeripheral && null != currentGatt)
        {
            sendBroadcast(BLE_STATE_CONNECTED, currentPeripheral);
            return;
        }

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                sendBroadcast(BLE_STATE_DISCONNECTED, currentPeripheral);
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

    /**
     * 停止扫描
     */
    public void stopScan()
    {
        this.targetPeripheral = null;
        this.isScanning = false;
        if(null != bluetoothAdapter)
        {
            bluetoothAdapter.stopLeScan(this);
        }
    }

    /**
     * 是否正在扫描
     * @return
     */
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

    /**
     * 扫描到蓝牙设备时的回调方法
     * @param device
     * @param rssi
     * @param scanRecord
     */
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

    /**
     * 连接到蓝牙设备时的回调方法
     * @param device
     */
    private synchronized void onDiscoveredTargetDevice(BluetoothDevice device)
    {
        if(null == currentPeripheral)
        {
            BluetoothGatt bluetoothGatt = device.connectGatt(TGApplication.getInstance(), false,
                    bluetoothGattCallback);
            currentPeripheral = new TGBLEPeripheralInfo();
            currentPeripheral.setPeripheralName(device.getName());
            currentPeripheral.setMacAddress(bluetoothGatt.getDevice().getAddress());
        }

        handler.sendEmptyMessage(MESSAGE_STOP_SCAN);
    }

    /**
     * 启动蓝牙开关设置界面
     * @param activity
     */
    public void turnOnBluetooth(Activity activity)
    {
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLUETOOTH);
        }
    }

    /**
     * 后台打开蓝牙
     */
    public void turnOnBluetoothInBackground()
    {
        bluetoothAdapter.enable();
    }

    /**
     * 后台关闭蓝牙
     */
    public void turnOffBluetoothInBackground()
    {
        bluetoothAdapter.disable();
    }

    /**
     * 获取当前连接设备的信息
     * @return
     */
    public TGBLEPeripheralInfo getCurrentPeripheral()
    {
        return currentPeripheral;
    }

    /**
     * 获取当前连接的设备
     * @return
     */
    protected BluetoothGatt getCurrentGatt()
    {
        return currentGatt;
    }

    /**
     * 获取上次连接设备的信息
     * @return
     */
    public TGBLEPeripheralInfo getLastPeripheral()
    {
        return lastPeripheral;
    }

    /**
     * 蓝牙设备变化回调，处理各种业务
     */
    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            currentGatt = gatt;
            LOG.d("[Method:onServicesDiscovered] status == " + status);
            TGBLEManager.this.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
            //监听characteristic的值变化
            LOG.d("[Method:onCharacteristicChanged]  characteristic == " + characteristic.getUuid() +
                    " value == " + characteristic.getValue()[0]);
            TGBLEManager.this.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            LOG.d("[Method:onCharacteristicRead] status == " + status + "  characteristic == " +
                    characteristic.getUuid() +  "  value == " + characteristic.getValue()[0]);
            TGBLEManager.this.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            LOG.d("[Method:onCharacteristicWrite] status == " + status + "  characteristic == " +
                    characteristic.getUuid() + "  value == " + characteristic.getValue()[0]);
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

                    lastPeripheral = currentPeripheral;
                    currentPeripheral = null;
                    sendBroadcast(BLE_STATE_DISCONNECTED, lastPeripheral);
                    break;
            }
        }
    };

    /**
     * 扫描到Service时的回调方法
     * @param gatt
     * @param status
     */
    protected void onServicesDiscovered(BluetoothGatt gatt, int status)
    {

    }

    /**
     * Characteristic的值发生变化时的回调方法
     * @param gatt
     * @param characteristic
     */
    protected void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {

    }

    /**
     * 读取Characteristic的值时的回调方法
     * @param gatt
     * @param characteristic
     * @param status
     */
    protected synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {

    }

    /**
     * 监听指定UUID的Characteristic的值变化
     * @param serviceUUID
     * @param characteristicUUID
     */
    public void listenCharacteristic(UUID serviceUUID, UUID characteristicUUID)
    {
        LOG.d("[Method:listenCharacteristic] serviceUUID == " + serviceUUID + " ; characteristicUUID == " + characteristicUUID);

        BluetoothGattService service = currentGatt.getService(serviceUUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
        currentGatt.setCharacteristicNotification(characteristic, true);

        for(BluetoothGattDescriptor descriptor:characteristic.getDescriptors())
        {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            currentGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * 读取指定UUID的Characteristic的值
     * @param serviceUUID
     * @param characteristicUUID
     */
    public void readCharacteristic(UUID serviceUUID, UUID characteristicUUID)
    {
        LOG.d("[Method:readCharacteristic] serviceUUID == " + serviceUUID + " ; characteristicUUID == " + characteristicUUID);

        BluetoothGattService service = currentGatt.getService(serviceUUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
        currentGatt.readCharacteristic(characteristic);
    }

    /**
     * 写入UUID的Characteristic的值
     * @param serviceUUID
     * @param characteristicUUID
     * @param value
     */
    public void writeCharacteristic(UUID serviceUUID, UUID characteristicUUID, byte value)
    {
        LOG.d("[Method:writeCharacteristic] serviceUUID == " + serviceUUID + " ; characteristicUUID == " + characteristicUUID);

        BluetoothGattService service = currentGatt.getService(serviceUUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);

        byte[] values = new byte[1];
        values[0] = value;
        characteristic.setValue(values);
        currentGatt.writeCharacteristic(characteristic);
    }

    /**
     * 发送广播，通知界面更新
     * @param bleState
     * @param peripheralInfo
     */
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

    /**
     * 从广播的Intent中读取蓝牙设备信息
     * @param data
     * @return
     */
    public static TGBLEPeripheralInfo getBLEPeripheralInfo(Intent data)
    {
        return (TGBLEPeripheralInfo)data.getSerializableExtra(PERIPHERAL_INFO_KEY);
    }

    /**
     * 从广播的Intent中，读取蓝牙状态信息
     * @param data
     * @return
     */
    public static int getBLEState(Intent data)
    {
        return data.getIntExtra(BLE_STATE_KEY, BLE_STATE_DISCONNECTED);
    }
}
