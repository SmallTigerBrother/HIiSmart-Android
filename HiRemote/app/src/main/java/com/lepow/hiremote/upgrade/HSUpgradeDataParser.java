package com.lepow.hiremote.upgrade;

import com.mn.tiger.upgrade.IUpgradeDataParser;

import org.json.JSONObject;

/**
 * Created by Dalang on 2015/8/1.
 */
public class HSUpgradeDataParser implements IUpgradeDataParser
{
    @Override
    public int getUpgradeMode(JSONObject upgradeData)
    {
        try
        {
            int status = upgradeData.getInt("status");
            switch (status)
            {
                case 1:
                    return MODE_NOT_YET;

                case 2:
                    return MODE_PROMPT;

                case 3:
                    return MODE_FORCE;

                case 4:
                    return MODE_INVALID;

                default:
                    return MODE_NOT_YET;
            }
        }
        catch (Exception e)
        {
            return MODE_NOT_YET;
        }
    }

    @Override
    public String getUpgradeDescription(JSONObject upgradeData)
    {
        try
        {
            return upgradeData.getString("description");
        }
        catch (Exception e)
        {
            return "";
        }
    }

    @Override
    public String getUpgradeLatestVersoin(JSONObject upgradeData)
    {
        try
        {
            return upgradeData.getString("latestVersion");
        }
        catch (Exception e)
        {
            return "";
        }
    }

    @Override
    public String getUpgradePackageUrl(JSONObject upgradeData)
    {
        try
        {
            return upgradeData.getString("description");
        }
        catch (Exception e)
        {
            return "";
        }
    }

    @Override
    public String getUpgradeOptions(JSONObject upgradeData)
    {
        try
        {
            return upgradeData.getString("options");
        }
        catch (Exception e)
        {
            return "";
        }
    }
}
