package com.lepow.hiremote.app;

import com.lepow.hiremote.request.HttpLoader;
import com.mn.tiger.request.HttpType;
import com.mn.tiger.upgrade.TGUpgradeManager;
import com.lepow.hiremote.upgrade.HSUpgradeDataParser;
import com.mn.tiger.app.TGApplication;

public class HSApplication extends TGApplication
{
    public static final int MODE_LOCATE = 1;

    public static final int MODE_CAPTURE = 2;

    public static final int MODE_RECORD= 3;

    private int mode = MODE_LOCATE;

    private int lastMode = MODE_LOCATE;

    @Override
    public void onCreate()
    {
        super.onCreate();

        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("appId",getPackageName());
        httpLoader.setHttpType(HttpType.REQUEST_POST);

        TGUpgradeManager.setUpgradeDataParser(new HSUpgradeDataParser());
        TGUpgradeManager.setCheckUpgradeHttpLoader(httpLoader);

        initMode();
    }

    private void initMode()
    {

    }

    public void setCaptureMode()
    {
        this.lastMode = this.mode;
        this.mode = MODE_CAPTURE;
    }

    public void resetMode()
    {
        this.mode = lastMode;
    }

    public int getMode()
    {
        return mode;
    }
}
