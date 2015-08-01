package com.mn.tiger.upgrade;

import android.content.Context;
import android.content.Intent;

import com.lepow.hiremote.request.Httploader;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.log.Logger;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.PackageUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dalang on 2015/8/1.
 */
public class TGUpgradeManager
{
    private static final Logger LOG = Logger.getLogger(TGUpgradeManager.class);

    static final String INTENT_KEY_UPGRADE_MODE = "upgrade_mode";

    static final String INTENT_KEY_UPGRADE_LATEST_VERSION = "upgrade_latest_version";

    static final String INTENT_KEY_UPGRADE_DESCRIPTION = "upgrade_description";

    static final String INTENT_KEY_UPGRADE_PACKAGE_URL = "upgrade_package_url";

    static final String INTENT_KEY_UPGRADE_OPTIONS = "upgrade_options";

    static final String UPGRADE_URL = "";

    private static IUpgradeDataParser upgradeDataParser;

    public static void upgrade()
    {
        Context context = TGApplication.getInstance();
        Httploader<Void> httploader = new Httploader<Void>();
        httploader.addRequestParam("appVersion", PackageUtils.getPackageInfoByName(context,context.getPackageName()).versionName);
        httploader.addRequestParam("system","android");
        httploader.addRequestParam("appId",context.getPackageName());
        httploader.loadByPost(context, UPGRADE_URL, Void.class, new Httploader.SimpleOnLoadCallback<Void>(context)
        {
            @Override
            public void onLoadSuccess(Void result, TGHttpResult tgHttpResult)
            {
                String resultString = tgHttpResult.getResult();
                try
                {
                    JSONObject upgradeData = new JSONObject(resultString);
                    if(null != upgradeDataParser)
                    {
                        int upgradeMode = upgradeDataParser.getUpgradeMode(upgradeData);
                        if(upgradeMode != IUpgradeDataParser.MODE_NOT_YET)
                        {
                            startUpgradeActivity(upgradeMode,
                                    upgradeDataParser.getUpgradeLatestVersoin(upgradeData),
                                    upgradeDataParser.getUpgradeDescription(upgradeData),
                                    upgradeDataParser.getUpgradePackageUrl(upgradeData),
                                    upgradeDataParser.getUpgradeOptions(upgradeData));
                        }
                    }
                }
                catch (JSONException e)
                {
                    LOG.e(e);
                }
            }
        });
    }

    private static void startUpgradeActivity(int upgradeMode, String latestVersion, String description,
                                             String packageUrl, String options)
    {
        Intent intent = new Intent(TGApplication.getInstance(), TGUpgradeDialogActivity.class);
        intent.putExtra(INTENT_KEY_UPGRADE_MODE, upgradeMode);
        intent.putExtra(INTENT_KEY_UPGRADE_LATEST_VERSION, latestVersion);
        intent.putExtra(INTENT_KEY_UPGRADE_DESCRIPTION,description);
        intent.putExtra(INTENT_KEY_UPGRADE_PACKAGE_URL, packageUrl);
        intent.putExtra(INTENT_KEY_UPGRADE_OPTIONS, options);

        TGApplication.getInstance().startActivity(intent);
    }

    public static void setUpgradeDataParser(IUpgradeDataParser policy)
    {
        upgradeDataParser = policy;
    }

    static int getUpgradeModeFromIntent(Intent intent)
    {
        return intent.getIntExtra(INTENT_KEY_UPGRADE_MODE, IUpgradeDataParser.MODE_NOT_YET);
    }

    static String getLatestVersionFromIntent(Intent intent)
    {
        return intent.getStringExtra(INTENT_KEY_UPGRADE_LATEST_VERSION);
    }

    static String getDesciptionFromIntent(Intent intent)
    {
        return intent.getStringExtra(INTENT_KEY_UPGRADE_DESCRIPTION);
    }

    static String getPackageUrlFromIntent(Intent intent)
    {
        return intent.getStringExtra(INTENT_KEY_UPGRADE_PACKAGE_URL);
    }

    static String getOptionsFromIntent(Intent intent)
    {
        return intent.getStringExtra(INTENT_KEY_UPGRADE_OPTIONS);
    }

}
