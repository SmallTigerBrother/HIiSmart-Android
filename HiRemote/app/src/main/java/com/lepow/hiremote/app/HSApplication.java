package com.lepow.hiremote.app;

import com.lepow.hiremote.misc.ServerUrls;
import com.lepow.hiremote.request.HttpLoader;
import com.lepow.hiremote.request.SimpleLoadCallback;
import com.mn.tiger.request.HttpType;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.upgrade.TGUpgradeManager;
import com.lepow.hiremote.upgrade.HSUpgradeDataParser;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.utility.PackageUtils;

public class HSApplication extends TGApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("appId","app123");
        httpLoader.setHttpType(HttpType.REQUEST_POST);

        TGUpgradeManager.setUpgradeDataParser(new HSUpgradeDataParser());
        TGUpgradeManager.setCheckUpgradeHttpLoader(httpLoader);
    }
}
