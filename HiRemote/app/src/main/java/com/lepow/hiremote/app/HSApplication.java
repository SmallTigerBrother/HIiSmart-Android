package com.lepow.hiremote.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lepow.hiremote.bluetooth.HSBLEPeripheralManager;
import com.lepow.hiremote.bluetooth.data.PeripheralDataManager;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.request.HttpLoader;
import com.lepow.hiremote.upgrade.HSUpgradeDataParser;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.location.ILocationManager;
import com.mn.tiger.location.TGLocationManager;
import com.mn.tiger.request.HttpType;
import com.mn.tiger.upgrade.TGUpgradeManager;

public class HSApplication extends TGApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("appId", getPackageName());
        httpLoader.setHttpType(HttpType.REQUEST_POST);

        TGUpgradeManager.setUpgradeDataParser(new HSUpgradeDataParser());
        TGUpgradeManager.setCheckUpgradeHttpLoader(httpLoader);

        TGLocationManager.init(ILocationManager.Provider.AMap);

        this.registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                PeripheralDataManager.savePeripheral(HSApplication.this, PeripheralInfo.fromBLEPeripheralInfo(
                        HSBLEPeripheralManager.getInstance().getCurrentPeripheral()));
            }
        }, new IntentFilter(IntentAction.ACTION_READ_PERIPHERAL_POWER));
    }
}
