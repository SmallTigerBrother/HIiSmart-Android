package com.lepow.hiremote.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.view.View;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.bluetooth.TGBLEManager;
import com.mn.tiger.log.Logger;

import java.util.UUID;

/**
 * Created by peng on 15/8/15.
 */
public class HSBLEPeripheralManager extends TGBLEManager
{
    private static final Logger LOG = Logger.getLogger(HSBLEPeripheralManager.class);

    private static final String POWER_CHARACTERISTIC_VALUE_KEY = "power_characteristic_value";

    private static final String DISCONNECT_ALARM_CHARACTERISTIC_VALUE_KEY = "disconnected_alarm_characteristic_value";

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

    private static final int MESSAGE_READ_POWER = 0x0004;

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

    protected HSBLEPeripheralManager()
    {
        HSApplication.getInstance().registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {

            }
        }, new IntentFilter(IntentAction.ACTION_READ_DISCONNECTED_ALARM));
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

            case MESSAGE_READ_POWER:
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
            //监听硬件寻找手机事件
            handler.sendEmptyMessage(MESSAGE_LISTEN_FIND_PHONE);
            try
            {
                Thread.sleep(2000);
                //读取电量
                handler.sendEmptyMessage(MESSAGE_READ_POWER);
            }
            catch (InterruptedException e)
            {
                LOG.e(e);
            }
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

    @Override
    protected synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        super.onCharacteristicRead(gatt, characteristic, status);

        if(characteristic.getUuid().toString().equals(DISCONNECT_ALARM_CHARACTERISTIC_UUID))
        {
            Intent intent = new Intent(IntentAction.ACTION_READ_DISCONNECTED_ALARM);
            byte value = characteristic.getValue()[0];
            intent.putExtra(DISCONNECT_ALARM_CHARACTERISTIC_VALUE_KEY, value != 0);
            HSApplication.getInstance().sendBroadcast(intent);

            //保存属性值
            getCurrentPeripheral().putValue(DISCONNECT_ALARM_CHARACTERISTIC_VALUE_KEY,value != 0);
        }
        else if(characteristic.getUuid().toString().equals(POWER_CHARACTERISTIC_UUID))
        {
            Intent intent = new Intent(IntentAction.ACTION_READ_PERIPHERAL_POWER);
            intent.putExtra(POWER_CHARACTERISTIC_VALUE_KEY, (int)characteristic.getValue()[0]);
            HSApplication.getInstance().sendBroadcast(intent);

            //保存属性值
            getCurrentPeripheral().setEnergy ((int)characteristic.getValue()[0]);

            //读取设备断开响铃设置
            handler.sendEmptyMessage(MESSAGE_READ_DISCONNECTED_ALARM);
        }
    }

    /**
     * 读取并监听电量变化
     */
    public void readAndListenPower()
    {
        readCharacteristic(UUID.fromString(HSBLEPeripheralManager.POWER_SERVICE_UUID),
                UUID.fromString(HSBLEPeripheralManager.POWER_CHARACTERISTIC_UUID));
    }

    /**
     * 读取连接断开响铃设置
     */
    private void readDisconnectAlarm()
    {
        readCharacteristic(UUID.fromString(HSBLEPeripheralManager.DISCONNECT_ALARM_SERVICE_UUID),
                UUID.fromString(HSBLEPeripheralManager.DISCONNECT_ALARM_CHARACTERISTIC_UUID));
    }

    /**
     * 监听蓝牙模块寻找手机变化
     */
    private void listenFindPhone()
    {
        listenCharacteristic(UUID.fromString(HSBLEPeripheralManager.FIND_PHONE_SERVICE_UUID),
                UUID.fromString(HSBLEPeripheralManager.FIND_PHONE_CHARACTERISTIC_UUID));
    }

    /**
     * 立刻打开蓝牙模块响铃
     */
    public void turnOnAlarmImmediately()
    {
        writeCharacteristic(UUID.fromString(ALARM_IMMEDIATELY_SERVICE_UUID),
                UUID.fromString(ALARM_IMMEDIATELY_CHARACTERISTIC_UUID), (byte) 2);
    }

    /**
     * 立刻关闭蓝牙模块响铃
     */
    public void turnOffAlarmImmediately()
    {
        writeCharacteristic(UUID.fromString(ALARM_IMMEDIATELY_SERVICE_UUID),
                UUID.fromString(ALARM_IMMEDIATELY_CHARACTERISTIC_UUID), (byte) 0);
    }

    /**
     * 开启蓝牙设备断开响铃
     */
    public void turnOnAlarmOfDisconnected()
    {
        writeCharacteristic(UUID.fromString(DISCONNECT_ALARM_SERVICE_UUID),
                UUID.fromString(DISCONNECT_ALARM_CHARACTERISTIC_UUID), (byte)2);
    }

    /**
     * 关闭蓝牙设备断开响铃
     */
    public void turnOffAlarmOfDisconnected()
    {
        writeCharacteristic(UUID.fromString(DISCONNECT_ALARM_SERVICE_UUID),
                UUID.fromString(DISCONNECT_ALARM_CHARACTERISTIC_UUID), (byte)0);
    }

    /**
     *读取蓝牙设备查找手机Characteristic的值
     * @param data
     * @return
     */
    public int getValueOfFindPhoneCharacteristic(Intent data)
    {
        return data.getIntExtra(FIND_PHONE_CHARACTERISTIC_VALUE_KEY, -1);
    }

    /**
     * 读取设备失去连接响铃开关的值
     * @param data
     * @return
     */
    public boolean getValueOfDisconnectedAlarmCharacteristic(Intent data)
    {
        return data.getBooleanExtra(DISCONNECT_ALARM_CHARACTERISTIC_VALUE_KEY, false);
    }

    public boolean isDisconnectedAlarmEnable()
    {
        if(null != getCurrentPeripheral())
        {
            return (Boolean)getCurrentPeripheral().getValue(DISCONNECT_ALARM_CHARACTERISTIC_VALUE_KEY, false);
        }
       return false;
    }

    public int getPeripheralPower()
    {
      if(null != getCurrentPeripheral())
      {
          return (Integer)getCurrentPeripheral().getValue(POWER_CHARACTERISTIC_VALUE_KEY, 50);
      }

        return 50;
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
