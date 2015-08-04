package com.mn.tiger.upgrade;

import android.content.Intent;

import com.lepow.hiremote.request.Response;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.log.Logger;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

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

    private static TGHttpLoader<Void> checkUpgradeHttpLoader;

    public static void upgrade(String url)
    {
        if(null == checkUpgradeHttpLoader || null == upgradeDataParser)
        {
            LOG.e("[Method:upgrade] you must set checkUpgradeHttpLoader and upgradeDataParser before upgrade");
            return;
        }

        checkUpgradeHttpLoader.load(TGApplication.getInstance(), url, Void.class, new TGHttpLoader.OnLoadCallback<Void>()
        {
            @Override
            public void onLoadStart()
            {
            }

            @Override
            public void onLoadSuccess(Void result, TGHttpResult tgHttpResult)
            {
                LOG.d("[Method:onLoadSuccess]");
                try
                {
                    JSONObject upgradeData = new JSONObject(((Response<Void>)tgHttpResult.getObjectResult()).rawData);
                    if (null != upgradeDataParser)
                    {
                        int upgradeMode = upgradeDataParser.getUpgradeMode(upgradeData);
                        if (upgradeMode != IUpgradeDataParser.MODE_NOT_YET)
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

            @Override
            public void onLoadError(int code, String message, TGHttpResult tgHttpResult)
            {
            }

            @Override
            public void onLoadCache(Void result, TGHttpResult tgHttpResult)
            {
            }

            @Override
            public void onLoadOver()
            {
            }
        });
    }

    private static void startUpgradeActivity(int upgradeMode, String latestVersion, String description,
                                             String packageUrl, String options)
    {
        LOG.d("[Method:startUpgradeActivity]");

        Intent intent = new Intent(TGApplication.getInstance(), TGUpgradeDialogActivity.class);
        intent.putExtra(INTENT_KEY_UPGRADE_MODE, upgradeMode);
        intent.putExtra(INTENT_KEY_UPGRADE_LATEST_VERSION, latestVersion);
        intent.putExtra(INTENT_KEY_UPGRADE_DESCRIPTION,description);
        intent.putExtra(INTENT_KEY_UPGRADE_PACKAGE_URL, packageUrl);
        intent.putExtra(INTENT_KEY_UPGRADE_OPTIONS, options);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TGApplication.getInstance().startActivity(intent);
    }

    public static void setUpgradeDataParser(IUpgradeDataParser upgradeDataParser)
    {
        TGUpgradeManager.upgradeDataParser = upgradeDataParser;
    }

    public static void setCheckUpgradeHttpLoader(TGHttpLoader<Void> checkUpgradeHttpLoader)
    {
        TGUpgradeManager.checkUpgradeHttpLoader = checkUpgradeHttpLoader;
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
