package com.mn.tiger.upgrade;

import org.json.JSONObject;

/**
 * Created by Dalang on 2015/8/1.
 */
public interface IUpgradeDataParser
{
    public static final int MODE_NOT_YET = 0;

    public static final int MODE_PROMPT = 1;

    public static final int MODE_FORCE = 2;

    public static final int MODE_INVALID = -1;

    int getUpgradeMode(JSONObject upgradeData);

    String getUpgradeDescription(JSONObject upgradeData);

    String getUpgradeLatestVersoin(JSONObject upgradeData);

    String getUpgradePackageUrl(JSONObject upgradeData);

    String getUpgradeOptions(JSONObject upgradeData);

}
