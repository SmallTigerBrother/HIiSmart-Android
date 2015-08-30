package com.lepow.hiremote.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.View;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.bluetooth.TGBLEManager;

import java.util.UUID;

/**
 * Created by peng on 15/8/15.
 */
public class HSBLEPeripheralManager extends TGBLEManager
{
    private static final String FIND_PHONE_CHARACTERISTIC_VALUE_KEY = "find_phone_characteristic_value";

    public static final String POWER_SERVICE_UUID = "0000180f-0000-1000-8000-00805f9b34fb";

    public static final String POWER_CHARACTERISTIC_UUID = "00002a19-0000-1000-8000-00805f9b34fb";

    public static final String DISCONNECT_ALARM_SERVICE_UUID = "00001803-0000-1000-8000-00805f9b34fb";

    public static final String DISCONNECT_ALARM_CHARACTERISTIC_UUID = "00002a06-0000-1000-8000-00805f9b34fb";

    public static final String ALARM_IMMEDIATELY_SERVICE_UUID = "00001802-0000-1000-8000-00805f9b34fb";

    public static final String ALARM_IMMEDIATELY_CHARACTERISTIC_UUID = "00002a06-0000-1000-8000-00805f9b34fb";

    public static final String FIND_PHONE_SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";

    public static final String FIND_PHONE_CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";

    /**
     * 短按找手机按钮（圆点）的值
     */
    public static final int FIND_PHONE_CHARACTERISTIC_VALUE = 1;

    /**
     * 长按找手机按钮（圆点）的值
     */
    public static final int FIND_PHONE_CHARACTERISTIC_VALUE_LONG = 2;

    private static final int MESSAGE_LISTEN_FIND_PHONE = 0x0002;

    private static final int MESSAGE_READ_DISCONNECTED_ALARM = 0x0003;

    private static final int MESSAGE_POWER = 0x0004;

    private static HSBLEPeripheralManager instance;

    public static HSBLEPeripheralManager getInstance()
    {
        if(null == instance)
        {
            synchronized (HSBLEPeripheralManager.class)
            {
                if(null == instance)
                {
                    instance = new HSBLEPeripheralManager();
                    //设置蓝牙扫描参数
//                    TGBLEScanParameter scanParameter = new TGBLEScanParameter();
//
//                    TGBLEScanParameter.TGBLEServiceParameter serviceParameter =
//                            new TGBLEScanParameter.TGBLEServiceParameter();
//                    serviceParameter.setUUID(UUID.fromString(""));
//                    scanParameter.addService(serviceParameter);
//
//                    TGBLEScanParameter.TGBLEServiceParameter serviceParameter_2 =
//                            new TGBLEScanParameter.TGBLEServiceParameter();
//                    serviceParameter_2.setUUID(UUID.fromString(""));
//                    scanParameter.addService(serviceParameter_2);
//
//                    TGBLEScanParameter.TGBLEServiceParameter serviceParameter_3 =
//                            new TGBLEScanParameter.TGBLEServiceParameter();
//                    serviceParameter_3.setUUID(UUID.fromString(""));
//                    scanParameter.addService(serviceParameter_3);

//                    instance.setScanParameter(scanParameter);
                }
            }
        }
        return instance;
    }

    @Override
    protected void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        switch (msg.what)
        {
            case MESSAGE_LISTEN_FIND_PHONE:
                listenFindPhone();
                break;

            case MESSAGE_READ_DISCONNECTED_ALARM:
                readDisconnectAlarm();
                break;

            case MESSAGE_POWER:
                readAndListenPower();
                break;
            default:
                break;
        }
    }

    /**
     * 扫描设备，并自动连接第一个设备
     */
    public void scanAndConnect2Peripheral()
    {
        scan();
    }

    public void scanAndConnect2NewPeripheral()
    {

    }

    @Override
    protected void onServicesDiscovered(BluetoothGatt gatt, int status)
    {
        if(status == BluetoothGatt.GATT_SUCCESS)
        {
            //读取电量
//            readAndListenPower();

            //监听硬件寻找手机事件
            handler.sendEmptyMessage(MESSAGE_LISTEN_FIND_PHONE);
            //读取设备断开响铃设置
//            handler.sendEmptyMessage(MESSAGE_READ_DISCONNECTED_ALARM);

//            handler.sendEmptyMessage(MESSAGE_POWER);
//            readDisconnectAlarm();
        }
    }

    @Override
    protected void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {
        super.onCharacteristicChanged(gatt, characteristic);

        Intent intent = new Intent(IntentAction.ACTION_FIND_PHONE);
        intent.putExtra(FIND_PHONE_CHARACTERISTIC_VALUE_KEY, (int)characteristic.getValue()[0]);
        HSApplication.getInstance().sendBroadcast(intent);
    }

    public void readAndListenPower()
    {
        readCharacteristic(UUID.fromString(HSBLEPeripheralManager.POWER_SERVICE_UUID),
                UUID.fromString(HSBLEPeripheralManager.POWER_CHARACTERISTIC_UUID));
    }

    private void readDisconnectAlarm()
    {
        readCharacteristic(UUID.fromString(HSBLEPeripheralManager.DISCONNECT_ALARM_SERVICE_UUID),
                UUID.fromString(HSBLEPeripheralManager.DISCONNECT_ALARM_CHARACTERISTIC_UUID));
    }

    private void listenFindPhone()
    {
        listenCharacteristic(UUID.fromString(HSBLEPeripheralManager.FIND_PHONE_SERVICE_UUID),
                UUID.fromString(HSBLEPeripheralManager.FIND_PHONE_CHARACTERISTIC_UUID));
    }

    public void turnOnAlarmImmediately()
    {
        writeCharacteristic(UUID.fromString(ALARM_IMMEDIATELY_SERVICE_UUID),
                UUID.fromString(ALARM_IMMEDIATELY_CHARACTERISTIC_UUID), (byte) 2);
    }

    public void turnOffAlarmImmediately()
    {
        writeCharacteristic(UUID.fromString(ALARM_IMMEDIATELY_SERVICE_UUID),
                UUID.fromString(ALARM_IMMEDIATELY_CHARACTERISTIC_UUID), (byte)0);
    }

    public int getValueOfCharacteristic(Intent data)
    {
        return data.getIntExtra(FIND_PHONE_CHARACTERISTIC_VALUE_KEY, -1);
    }

    /**
     * 显示未找到新设备的对话框
     * @param activity
     */
    public void showNotFoundNewDeviceDialog(final Activity activity)
    {
        HSAlertDialog dialog = new HSAlertDialog(activity);
        dialog.setTitleVisibility(View.GONE);
        dialog.setBodyText(activity.getString(R.string.not_found_new_peripheral_tips));
        dialog.setLeftButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        dialog.setRightButton(activity.getString(R.string.connect_old_peripheral), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //连接上一次连接的设备
                if(null != getLastPeripheral())
                {
                    scanAndConnect2Peripheral(getLastPeripheral().getPeripheralName());
                }
                else
                {
                    scanAndConnect2Peripheral();
                }
            }
        });
        dialog.show();
    }

    /**
     * 显示蓝牙未开启对话框
     */
    public void showBluetoothOffDialog(final Activity activity)
    {
        HSAlertDialog dialog = new HSAlertDialog(activity);
        dialog.setTitleVisibility(View.GONE);
        dialog.setBodyText(activity.getString(R.string.bluetooth_off_tips));
        dialog.setLeftButton(activity.getString(R.string.settings), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                HSBLEPeripheralManager.this.turnOnBluetooth(activity);
            }
        });
        dialog.setRightButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 显示不支持蓝牙4.0的对话框
     */
    public void showNonSupportBLEDialog(Activity activity)
    {
        HSAlertDialog dialog = new HSAlertDialog(activity);
        dialog.setTitleVisibility(View.GONE);
        dialog.setBodyText(activity.getString(R.string.nonsupport_bluetooth_tips));
        dialog.setMiddleButton(activity.getString(R.string.exit_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                HSApplication.getInstance().exit();
            }
        });
        dialog.show();
    }
}
