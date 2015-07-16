package com.lepow.hiremote.app;

import android.content.Context;

import com.mn.tiger.utility.Preferences;

public class AppSettings
{
	private static final String APP_SETTINGS_SHARE_PRES_NAME = "app_setting";
	
	private static final String CONNECT_SETTING_KEY = "connect";
	
	private static final String FIND_SETTING_KEY = "find";
	
	private static final String NOTIFICATION_SETTING_KEY = "notification";
	
	private static final String VOICE_SETTING_KEY = "voice";
	
	public static void setConnectedSetting(Context context, boolean isOn)
	{
		Preferences.save(context, APP_SETTINGS_SHARE_PRES_NAME, CONNECT_SETTING_KEY, isOn);
	}
	
	public static boolean isConnectedSettingOn(Context context)
	{
		return Preferences.read(context, APP_SETTINGS_SHARE_PRES_NAME, CONNECT_SETTING_KEY, true);
	}
	
	public static void setFindSetting(Context context, boolean isOn)
	{
		Preferences.save(context, APP_SETTINGS_SHARE_PRES_NAME, FIND_SETTING_KEY, isOn);
	}
	
	public static boolean isFindSettingOn(Context context)
	{
		return Preferences.read(context, APP_SETTINGS_SHARE_PRES_NAME, FIND_SETTING_KEY, true);
	}
	
	public static void setNotificationSetting(Context context, boolean isOn)
	{
		Preferences.save(context, APP_SETTINGS_SHARE_PRES_NAME, NOTIFICATION_SETTING_KEY, isOn);
	}
	
	public static boolean isNotificationSettingOn(Context context)
	{
		return Preferences.read(context, APP_SETTINGS_SHARE_PRES_NAME, NOTIFICATION_SETTING_KEY, true);
	}
	
	public static void setVoiceSetting(Context context, boolean isOn)
	{
		Preferences.save(context, APP_SETTINGS_SHARE_PRES_NAME, VOICE_SETTING_KEY, isOn);
	}
	
	public static boolean isVoiceSettingOn(Context context)
	{
		return Preferences.read(context, APP_SETTINGS_SHARE_PRES_NAME, VOICE_SETTING_KEY, true);
	}
}
