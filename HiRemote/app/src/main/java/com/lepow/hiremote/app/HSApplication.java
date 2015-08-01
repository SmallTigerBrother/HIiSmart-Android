package com.lepow.hiremote.app;

import com.mn.tiger.upgrade.TGUpgradeManager;
import com.lepow.hiremote.upgrade.HSUpgradeDataParser;
import com.mn.tiger.app.TGApplication;

public class HSApplication extends TGApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        TGUpgradeManager.setUpgradeDataParser(new HSUpgradeDataParser());
        TGUpgradeManager.upgrade();
    }
}
