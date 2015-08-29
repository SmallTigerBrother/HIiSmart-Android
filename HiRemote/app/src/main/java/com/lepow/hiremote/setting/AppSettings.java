package com.lepow.hiremote.setting;

import android.content.Context;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.utility.Preferences;

public class AppSettings
{
	private static final String APP_SETTINGS_SHARE_PRES_NAME = "app_setting";
	
	private static final String PLAY_SOUND_SETTING_KEY = "play_sound";
	
	private static final String PUSH_NOTIFICATION_SETTING_KEY = "push_notification";
	
	private static final String SETTINGS_MODE_KEY = "mode";

	public static final int MODE_LOCATE = 1;

	public static final int MODE_CAPTURE = 2;

	public static final int MODE_RECORD= 3;

	private static int mode = -1;

	private static int lastMode = mode;

	public static void switchToRecordMode()
	{
		mode = MODE_RECORD;
		Preferences.save(TGApplication.getInstance(), APP_SETTINGS_SHARE_PRES_NAME, SETTINGS_MODE_KEY, mode);
	}

	public static void switchToLocateMode()
	{
		mode = MODE_LOCATE;
		Preferences.save(TGApplication.getInstance(), APP_SETTINGS_SHARE_PRES_NAME, SETTINGS_MODE_KEY, mode);
	}

	public static void switchToCaptureMode()
	{
		lastMode = mode;
		mode = MODE_CAPTURE;
	}

	public static void resetModeFromCaptureMde()
	{
		mode = lastMode;
	}

	public static int getMode()
	{
		if(mode < 0)
		{
			mode = Preferences.read(TGApplication.getInstance(), APP_SETTINGS_SHARE_PRES_NAME, SETTINGS_MODE_KEY, MODE_LOCATE);
		}

		return mode;
	}
	
	public static void setPlaySoundSetting(Context context, boolean isOn)
	{
		Preferences.save(context, APP_SETTINGS_SHARE_PRES_NAME, PLAY_SOUND_SETTING_KEY, isOn);
	}
	
	public static boolean isPlaySoundSettingOn(Context context)
	{
		return Preferences.read(context, APP_SETTINGS_SHARE_PRES_NAME, PLAY_SOUND_SETTING_KEY, true);
	}
	
	public static void setPushNotificationSetting(Context context, boolean isOn)
	{
		Preferences.save(context, APP_SETTINGS_SHARE_PRES_NAME, PUSH_NOTIFICATION_SETTING_KEY, isOn);
	}
	
	public static boolean isPushNotificationSettingOn(Context context)
	{
		return Preferences.read(context, APP_SETTINGS_SHARE_PRES_NAME, PUSH_NOTIFICATION_SETTING_KEY, true);
	}
}
