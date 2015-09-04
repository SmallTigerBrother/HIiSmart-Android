package com.lepow.hiremote.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.android.camera.Camera;
import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.authorise.LoginActivity;
import com.lepow.hiremote.bluetooth.HSBLEPeripheralManager;
import com.lepow.hiremote.bluetooth.data.PeripheralDataManager;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.home.widget.PeripheralStatusView;
import com.lepow.hiremote.lbs.FindMyItemActivity;
import com.lepow.hiremote.lbs.PinnedLocationHistoryActivity;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.misc.IntentKeys;
import com.lepow.hiremote.misc.ServerUrls;
import com.lepow.hiremote.notification.NotificationManager;
import com.lepow.hiremote.record.VoiceMemosActivity;
import com.lepow.hiremote.setting.AppSettings;
import com.lepow.hiremote.setting.SettingActivity;
import com.mn.tiger.bluetooth.TGBLEManager;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;
import com.mn.tiger.log.Logger;
import com.mn.tiger.notification.TGNotificationBuilder;
import com.mn.tiger.upgrade.TGUpgradeManager;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView.ViewPagerHolder;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements View.OnClickListener
{
	private static final Logger LOG = Logger.getLogger(HomeActivity.class);

	@FindView(R.id.devices_viewpager)
	DotIndicatorBannerPagerView<PeripheralInfo> bannerPagerView;

	@FindView(R.id.common_function_btn)
	Button functionBtn;

	@FindView(R.id.common_function_bord)
	LinearLayout functionBord;

	@FindView(R.id.function_pinned_location_image)
	Button pinnedLocationImg;

	@FindView(R.id.function_camera_shutter_image)
	Button cameraShutterImg;

	@FindView(R.id.function_find_item_image)
	Button findItemImg;

	@FindView(R.id.function_voice_memos_image)
	Button voiceMemosImg;

	@FindView(R.id.common_settings_btn)
	Button settingsBtn;

	@FindView(R.id.common_settings_bord)
	LinearLayout settingsBord;

	@FindView(R.id.notification_switch)
	Switch notificationSwitch;

	@FindView(R.id.voice_switch)
	Switch voiceSwitch;

	@FindView(R.id.play_sound_switch)
	Switch playSoundSwitch;

	private Button lastClickFunction;

	private PeripheralInfo connectedPeripheral;

	private HashMap<Integer, Integer> defaultFunctionBtnResMap;

	private HashMap<Integer, Integer> highlightFunctionBtnResMap;

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals(IntentAction.ACTION_READ_PERIPHERAL_POWER))
			{
				connectedPeripheral = PeripheralInfo.fromBLEPeripheralInfo(
						HSBLEPeripheralManager.getInstance().getCurrentPeripheral());
				LOG.d("Peripheral power ");
				PeripheralDataManager.savePeripheral(HomeActivity.this, connectedPeripheral);
				onPeripheralChanged();
			}
			else if(intent.getAction().equals(IntentAction.ACTION_READ_DISCONNECTED_ALARM))
			{
				playSoundSwitch.setChecked(HSBLEPeripheralManager.getInstance().getValueOfDisconnectedAlarmCharacteristic(intent));
			}
			else if(intent.getAction().equals(TGBLEManager.ACTION_BLE_STATE_CHANGE))
			{
				int bleState = TGBLEManager.getBLEState(intent);
				final TGBLEPeripheralInfo peripheralInfo = TGBLEManager.getBLEPeripheralInfo(intent);
				switch (bleState)
				{
					case TGBLEManager.BLE_STATE_CONNECTED:
						connectedPeripheral = PeripheralInfo.fromBLEPeripheralInfo(
								HSBLEPeripheralManager.getInstance().getCurrentPeripheral());
						PeripheralDataManager.savePeripheral(HomeActivity.this, connectedPeripheral);
						onPeripheralChanged();
						break;
					case TGBLEManager.BLE_STATE_DISCONNECTED:
						connectedPeripheral = PeripheralInfo.fromBLEPeripheralInfo(
								HSBLEPeripheralManager.getInstance().getCurrentPeripheral());
						PeripheralDataManager.savePeripheral(HomeActivity.this, connectedPeripheral);
						onPeripheralChanged();
						showDisconnectedNotification();
						break;

					case TGBLEManager.BLE_STATE_NONSUPPORT:
						HSBLEPeripheralManager.getInstance().showBluetoothOffDialog(HomeActivity.this);
						break;

					default:
						break;
				}
			}
		}

		private void showDisconnectedNotification()
		{
			TGNotificationBuilder builder = new TGNotificationBuilder(HomeActivity.this);
			builder.setContentTitle("提示");
			builder.setContentText("aaaaaaaaa");
			builder.setSmallIcon(R.drawable.add_device);
			NotificationManager.getInstanse().showNotification(HomeActivity.this, 0 ,builder);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().getDecorView().setBackgroundResource(R.drawable.home_page_bg);
		setBarTitleText(getString(R.string.app_name));

		ButterKnife.bind(this);

		connectedPeripheral = (PeripheralInfo)getIntent().getSerializableExtra(IntentKeys.PERIPHERAL_INFO);

		initViews();

		//检测更新
		TGUpgradeManager.upgrade(ServerUrls.CHECK_UPGRADE_URL);
		this.registerReceiver(broadcastReceiver, new IntentFilter(IntentAction.ACTION_READ_PERIPHERAL_POWER));
		this.registerReceiver(broadcastReceiver, new IntentFilter(IntentAction.ACTION_READ_DISCONNECTED_ALARM));
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundColor(Color.TRANSPARENT);
		navigationBar.getMiddleTextView().setTextColor(Color.WHITE);
	}

	private void initViews()
	{
		showRightBarButton(true);
		getRightBarButton().setImageResource(R.drawable.navi_setting);
		getRightBarButton().setOnClickListener(this);

		showLeftBarButton(true);
		getLeftBarButton().setImageResource(R.drawable.add_device);
		getLeftBarButton().setOnClickListener(this);

		bannerPagerView.setViewPagerHolder(new ViewPagerHolder<PeripheralInfo>()
		{
			@Override
			public View initViewOfPage(Activity activity, PagerAdapter adapter, int viewType)
			{
				return new PeripheralStatusView(activity);
			}

			@Override
			public void fillData(View viewOfPage, PeripheralInfo itemData, int position, int viewType)
			{
				((PeripheralStatusView) viewOfPage).setData(itemData);
			}
		});

		showFunctionBoard();

		initSettingsBoard();

		onPeripheralChanged();
	}

	private void onPeripheralChanged()
	{
		playSoundSwitch.setChecked(HSBLEPeripheralManager.getInstance().isDisconnectedAlarmEnable());
		List<PeripheralInfo> peripheralInfos = PeripheralDataManager.getAllPeripherals(this, connectedPeripheral);
		bannerPagerView.setData(PeripheralDataManager.getAllPeripherals(this, connectedPeripheral));
		if(null != connectedPeripheral)
		{
			bannerPagerView.setCurrentPage(peripheralInfos.indexOf(connectedPeripheral));
		}
	}

	private void initSettingsBoard()
	{
		notificationSwitch.setChecked(AppSettings.isPushNotificationSettingOn(this));
		voiceSwitch.setChecked(AppSettings.getMode() == AppSettings.MODE_RECORD);
	}

	private void initFunctionBtnRes()
	{
		defaultFunctionBtnResMap.put(R.id.function_pinned_location_image, R.drawable.location_button_bg);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		connectedPeripheral = (PeripheralInfo)getIntent().getSerializableExtra(IntentKeys.PERIPHERAL_INFO);
		onPeripheralChanged();
	}

	@OnClick({ R.id.common_function_btn, R.id.common_settings_btn , R.id.notification_switch, R.id.voice_switch,
	R.id.function_pinned_location_image, R.id.function_camera_shutter_image, R.id.function_find_item_image,
	R.id.function_voice_memos_image})
	public void onClick(View view)
	{
		if(view == getRightBarButton())
		{
			startActivity(new Intent(this, SettingActivity.class));
			return;
		}
		else if(view == getLeftBarButton())
		{
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}

		switch (view.getId())
		{
			case R.id.common_function_btn:
				showFunctionBoard();
				break;

			case R.id.common_settings_btn:
				showSettingBoard();
				break;

			case R.id.function_pinned_location_image:
				pinnedLocationImg.setPressed(true);
				startPinnedLocationHistoryActivity();
				break;

			case R.id.function_camera_shutter_image:
				startCamera();
				break;

			case R.id.function_find_item_image:
				startFindItemActivity();
				break;

			case R.id.function_voice_memos_image:
				startVoiceMemosActivity();
				break;

			default:
				break; 
		}
	}

	@OnCheckedChanged({R.id.notification_switch, R.id.voice_switch, R.id.play_sound_switch})
	void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		switch (buttonView.getId())
		{
			case R.id.voice_switch:
				if(isChecked)
				{
					AppSettings.switchToRecordMode();
				}
				else
				{
					AppSettings.switchToLocateMode();
				}
				break;
			case R.id.notification_switch:
				AppSettings.setPushNotificationSetting(this, isChecked);
				break;

			case R.id.play_sound_switch:
				if(isChecked)
				{
					HSBLEPeripheralManager.getInstance().turnOnAlarmOfDisconnected();
				}
				else
				{
					HSBLEPeripheralManager.getInstance().turnOffAlarmOfDisconnected();
				}
				break;

			default:
				break;
		}
	}

	private void showFunctionBoard()
	{
		functionBord.setVisibility(View.VISIBLE);
		settingsBord.setVisibility(View.GONE);
		functionBtn.setBackgroundColor(getResources().getColor(R.color.color_val_4fc191));
		functionBtn.setTextColor(Color.WHITE);

		settingsBtn.setBackgroundResource(R.drawable.rectangle_4fc191_stroke_white_bg);
		settingsBtn.setTextColor(getResources().getColor(R.color.color_val_4fc191));
	}

	private void showSettingBoard()
	{
		functionBord.setVisibility(View.GONE);
		settingsBord.setVisibility(View.VISIBLE);

		settingsBtn.setBackgroundColor(getResources().getColor(R.color.color_val_4fc191));
		settingsBtn.setTextColor(Color.WHITE);

		functionBtn.setBackgroundResource(R.drawable.rectangle_4fc191_stroke_white_bg);
		functionBtn.setTextColor(getResources().getColor(R.color.color_val_4fc191));
	}

	private void startPinnedLocationHistoryActivity()
	{
		startActivity(new Intent(this, PinnedLocationHistoryActivity.class));
	}

	private void startFindItemActivity()
	{
		startActivity(new Intent(this, FindMyItemActivity.class));
	}

	private void startVoiceMemosActivity()
	{
		startActivity(new Intent(this, VoiceMemosActivity.class));
	}

	private void startCamera()
	{
		startActivity(Camera.class);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
