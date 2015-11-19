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

import com.mn.tiger.app.TGApplicationProxy;
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
     * BLE状态——找不到设备
     */
    public static final int BLE_STATE_NO_PERIPHERAL_FOUND = 6;

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

    /**
     * 当前连接蓝牙设备
     */
    private BluetoothGatt currentGatt;

    /**
     * 当前扫描状态
     */
    private BLEScannerState scannerState = BLEScannerState.STOP;

    /**
     * 指定要连接的设备地址信息
     */
    private String targetPeripheralMacAddress = null;

    /**
     * 停止扫描Message的what值
     */
    public static final int MESSAGE_STOP_SCAN = 0x0001;

    /**
     * 开始扫描
     */
    public static final int MESSAGE_START_SCAN = 0x0002;

    /**
     * 开始扫描新设备
     */
    public static final int MESSAGE_START_SCAN_NEW_PERIPHERAL = 0x0003;

    /**
     * 开始扫描指定设备
     */
    private static final int MESSAGE_START_SCAN_TARGET_PERIPHERAL = 0x0004;

    /**
     * 正在连接中
     */
    private boolean connecting = false;

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

            case MESSAGE_START_SCAN:
                scan();
                break;

            case MESSAGE_START_SCAN_NEW_PERIPHERAL:
                scanNewPeripheral();
                break;

            case MESSAGE_START_SCAN_TARGET_PERIPHERAL:
                scanTargetPeripheral(this.targetPeripheralMacAddress);
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
        if(TGApplicationProxy.getInstance().getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
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
            bluetoothAdapter = ((BluetoothManager)TGApplicationProxy.getInstance().getApplication().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            //注册广播监听蓝牙关闭、打开事件
            TGApplicationProxy.getInstance().getApplication().registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (state == BluetoothAdapter.STATE_OFF)
                    {
                        connecting = false;
                        scannerState = BLEScannerState.STOP;
                        sendBroadcast(BLE_STATE_POWER_OFF, null);
                    }
                    else if (state == BluetoothAdapter.STATE_ON)
                    {
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                sendBroadcast(BLE_STATE_POWER_ON, null);
                                scan();
                            }
                        }, 3000);
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
        if(scannerState == BLEScannerState.SCANNING)
        {
            return;
        }
        LOG.d("[Method:scan]");

        scannerState = BLEScannerState.SCANNING;
        this.targetPeripheralMacAddress = null;
        if(!bluetoothAdapter.isEnabled())
        {
            sendBroadcast(BLE_STATE_POWER_OFF, null);
            stopScan();
            return;
        }

        if(null != currentPeripheral && null != currentGatt)
        {
            sendBroadcast(BLE_STATE_CONNECTED, currentPeripheral);
            stopScan();
            return;
        }

        executeScan();
    }

    /**
     * 扫描并连接指定UUID的设备
     * @param peripheralMacAddress
     */
    public void scanTargetPeripheral(String peripheralMacAddress)
    {
        LOG.d("[Method:scanTargetPeripheral] peripheralMacAddress == " + peripheralMacAddress);
        this.targetPeripheralMacAddress = peripheralMacAddress;
        this.scannerState = BLEScannerState.SCANNING_TARGET_PERIPHERAL;
        if(!bluetoothAdapter.isEnabled())
        {
            sendBroadcast(BLE_STATE_POWER_OFF, null);
            stopScan();
            return;
        }

        if(null != currentPeripheral && null != currentGatt && currentPeripheral.getMacAddress().equals(peripheralMacAddress))
        {
            sendBroadcast(BLE_STATE_CONNECTED, currentPeripheral);
            stopScan();
            return;
        }

        if(null != currentGatt)
        {
            currentGatt.disconnect();
        }
        else
        {
            if(!TextUtils.isEmpty(peripheralMacAddress))
            {
                final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(peripheralMacAddress);
                if (null != device)
                {
                    final BluetoothGatt bluetoothGatt = device.connectGatt(TGApplicationProxy.getInstance().getApplication(), false,
                            bluetoothGattCallback);
                    if(!connecting)
                    {
                        connecting = true;
                        LOG.d("[Method:scanTargetPeripheral] connect peripheral directly");
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                LOG.d("[Method:scanTargetPeripheral] connect time out");
                                if (null == currentPeripheral)
                                {
                                    sendBroadcast(BLE_STATE_NO_PERIPHERAL_FOUND, currentPeripheral);
                                }
                                stopScan();
                            }
                        }, SCAN_TIME_OUT);
                    }
                }
                else
                {
                    sendBroadcast(BLE_STATE_NO_PERIPHERAL_FOUND, currentPeripheral);
                    stopScan();
                }
            }
            else
            {
                sendBroadcast(BLE_STATE_NO_PERIPHERAL_FOUND, currentPeripheral);
                stopScan();
            }
        }
    }

    /**
     * 扫描并连接指定UUID的设备
     */
    public void scanNewPeripheral()
    {
        LOG.d("[Method:scanNewPeripheral]");
        this.targetPeripheralMacAddress = null;
        this.scannerState = BLEScannerState.SCANNING_NEW_PERIPHERAL;
        if(!bluetoothAdapter.isEnabled())
        {
            sendBroadcast(BLE_STATE_POWER_OFF, null);
            stopScan();
            return;
        }

        if(null != currentGatt)
        {
            currentGatt.disconnect();
        }
        else
        {
            executeScan();
        }
    }

    private void executeScan()
    {
        if(!connecting)
        {
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    LOG.d("[Method:executeScan] Scan time out");
                    if (null == currentPeripheral)
                    {
                        sendBroadcast(BLE_STATE_NO_PERIPHERAL_FOUND, currentPeripheral);
                    }
                    stopScan();
                }
            }, SCAN_TIME_OUT);

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
    }

    public void setScanParameter(TGBLEScanParameter parameter)
    {
        this.scanParameter = parameter;
    }

    /**
     * 停止扫描
     */
    public void stopScan()
    {
        LOG.d("[Method:stopScan]");
        this.targetPeripheralMacAddress = null;
        connecting = false;
        this.scannerState = BLEScannerState.STOP;
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
        return scannerState != BLEScannerState.STOP;
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
        switch (scannerState)
        {
            case SCANNING_NEW_PERIPHERAL:
                if(null == lastPeripheral || (null != lastPeripheral && !lastPeripheral.getMacAddress().equals(device.getAddress())))
                {
                    onDiscoveredTargetDevice(device);
                }
                break;

            case SCANNING_TARGET_PERIPHERAL:
                if(null == this.targetPeripheralMacAddress || device.getAddress().equals(this.targetPeripheralMacAddress))
                {
                    onDiscoveredTargetDevice(device);
                }
                break;

            case SCANNING:
                onDiscoveredTargetDevice(device);
                break;

            default:
                break;
        }
    }

    /**
     * 连接到蓝牙设备时的回调方法
     * @param device
     */
    private synchronized void onDiscoveredTargetDevice(BluetoothDevice device)
    {
        if(null == currentPeripheral && !connecting)
        {
            connecting = true;
            device.connectGatt(TGApplicationProxy.getInstance().getApplication(), false,
                    bluetoothGattCallback);
        }
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
                    connecting = false;
                    currentPeripheral = new TGBLEPeripheralInfo();
                    currentPeripheral.setPeripheralName(gatt.getDevice().getName());
                    currentPeripheral.setMacAddress(gatt.getDevice().getAddress());
                    lastPeripheral = (null != lastPeripheral && currentPeripheral.getMacAddress().equals(lastPeripheral.getMacAddress())) ? null : lastPeripheral;

                    LOG.d("[Method:onConnectionStateChange] STATE_CONNECTED  mac == " + gatt.getDevice().getAddress());
                    gatt.discoverServices();
                    sendBroadcast(BLE_STATE_CONNECTED, currentPeripheral);

                    //停止扫描
                    handler.sendEmptyMessage(MESSAGE_STOP_SCAN);
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    gatt.close();
                    LOG.d("[Method:onConnectionStateChange] STATE_DISCONNECTED  mac == " + gatt.getDevice().getAddress());
                    lastPeripheral = (null != currentPeripheral) ? (TGBLEPeripheralInfo)currentPeripheral.clone() : lastPeripheral;
                    currentPeripheral = null;
                    currentGatt = null;

                    //继续扫描
                    switch (scannerState)
                    {
                        case SCANNING:
                            LOG.d("[Method:onConnectionStateChange] scannerState == SCANNING");
                            handler.sendEmptyMessage(MESSAGE_START_SCAN);
                            break;
                        case SCANNING_NEW_PERIPHERAL:
                            LOG.d("[Method:onConnectionStateChange] scannerState == SCANNING_NEW_PERIPHERAL");
                            handler.sendEmptyMessage(MESSAGE_START_SCAN_NEW_PERIPHERAL);
                            break;
                        case SCANNING_TARGET_PERIPHERAL:
                            LOG.d("[Method:onConnectionStateChange] scannerState == SCANNING_TARGET_PERIPHERAL   targetPeripheral == " + targetPeripheralMacAddress);
                            handler.sendEmptyMessage(MESSAGE_START_SCAN_TARGET_PERIPHERAL);
                            break;
                        default:
                            LOG.d("[Method:onConnectionStateChange] scannerState == STOP");
                            connecting = false;
                            sendBroadcast(BLE_STATE_DISCONNECTED, lastPeripheral);
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 发送广播，通知界面更新
     * @param bleState
     * @param peripheralInfo
     */
    private synchronized static void sendBroadcast(int bleState, TGBLEPeripheralInfo peripheralInfo)
    {
        LOG.d("[Method:sendBroadcast] State == " + bleState);

        Intent intent = new Intent();
        intent.setAction(ACTION_BLE_STATE_CHANGE);
        intent.putExtra(BLE_STATE_KEY, bleState);
        if(null != peripheralInfo)
        {
            intent.putExtra(PERIPHERAL_INFO_KEY, peripheralInfo);
        }
        TGApplicationProxy.getInstance().getApplication().sendBroadcast(intent);
    }

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

        if(null != currentGatt)
        {
            BluetoothGattService service = currentGatt.getService(serviceUUID);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            currentGatt.setCharacteristicNotification(characteristic, true);

            for(BluetoothGattDescriptor descriptor:characteristic.getDescriptors())
            {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                currentGatt.writeDescriptor(descriptor);
            }
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

        if(null != currentGatt)
        {
            BluetoothGattService service = currentGatt.getService(serviceUUID);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            currentGatt.readCharacteristic(characteristic);
        }
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

        if(null != currentGatt)
        {
            BluetoothGattService service = currentGatt.getService(serviceUUID);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);

            byte[] values = new byte[1];
            values[0] = value;
            characteristic.setValue(values);
            currentGatt.writeCharacteristic(characteristic);
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

    public enum BLEScannerState
    {
        SCANNING,
        SCANNING_NEW_PERIPHERAL,
        SCANNING_TARGET_PERIPHERAL,
        STOP
    }
}
