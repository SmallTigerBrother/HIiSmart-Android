package com.lepow.hiremote.bluetooth;

import android.app.Activity;
import android.content.DialogInterface;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.bluetooth.TGBluetoothManager;

/**
 * Created by peng on 15/8/15.
 */
public class HSBLEPeripheralManager extends TGBluetoothManager
{
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
                }
            }
        }
        return instance;
    }

    /**
     * 扫描设备，并自动连接第一个设备
     */
    public void scanAndConnect2Peripheral()
    {

    }

    public void scanAndConnect2NewPeripheral()
    {

    }

    public void showNotFoundNewDeviceDialog(final Activity activity)
    {
        HSAlertDialog dialog = new HSAlertDialog(activity);
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
                scanAndConnect2Peripheral(getLastPeripheral().getUUID());
            }
        });
    }

    /**
     * 显示蓝牙未开启对话框
     */
    public void showBluetoothOffDialog(final Activity activity)
    {
        HSAlertDialog dialog = new HSAlertDialog(activity);
        dialog.setBodyText(activity.getString(R.string.bluetooth_off_tips));
        dialog.setLeftButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        dialog.setRightButton(activity.getString(R.string.open_bluetooth_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                HSBLEPeripheralManager.this.turnOnBluetooth(activity);
            }
        });
    }

    /**
     * 显示不支持蓝牙4.0的对话框
     */
    public void showNonSupportBLEDialog(Activity activity)
    {
        HSAlertDialog dialog = new HSAlertDialog(activity);
        dialog.setBodyText(activity.getString(R.string.nonsupport_bluetooth_tips));
        dialog.setLeftButton(activity.getString(R.string.exit_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                HSApplication.getInstance().exit();
            }
        });
    }
}
