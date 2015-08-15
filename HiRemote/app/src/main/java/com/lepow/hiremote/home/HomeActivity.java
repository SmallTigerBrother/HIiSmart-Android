package com.lepow.hiremote.home;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;

import com.lepow.hiremote.misc.ServerUrls;
import com.mn.tiger.upgrade.TGUpgradeManager;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView.ViewPagerHolder;
import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.home.present.HomePresenter;
import com.lepow.hiremote.home.present.IHomeView;

public class HomeActivity extends BaseActivity implements IHomeView
{
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

	private HomePresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		initViews();

		presenter = new HomePresenter(this, this);

		presenter.initDevicesAndSettings();

		//检测更新
		TGUpgradeManager.upgrade(ServerUrls.CHECK_UPGRADE_URL);

		presenter.register2Bus();
	}

	private void initViews()
	{
		bannerPagerView.setViewPagerHolder(new ViewPagerHolder<PeripheralInfo>()
		{
			@Override
			public View initViewOfPage(Activity activity, PagerAdapter adapter, int viewType)
			{
				return LayoutInflater.from(HomeActivity.this).inflate(R.layout.device_banner_item, null);
			}
			
			@Override
			public void fillData(View viewOfPage, PeripheralInfo itemData, int position, int viewType)
			{
				TextView deviceName = (TextView) viewOfPage.findViewById(R.id.device_name);
				deviceName.setText(itemData.getPeripheralName());

				TextView syncTimeView = (TextView) viewOfPage.findViewById(R.id.sync_time);
				syncTimeView.setText(itemData.getSyncTime() + "");
			}
		});
	}

	@OnClick({ R.id.common_function_btn, R.id.common_settings_btn , R.id.notification_switch, R.id.voice_switch})
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.common_function_btn:
				functionBord.setVisibility(View.VISIBLE);
				settingsBord.setVisibility(View.GONE);
				break;

			case R.id.common_settings_btn:
				functionBord.setVisibility(View.GONE);
				settingsBord.setVisibility(View.VISIBLE);
				break;

			case R.id.notification_switch:
				presenter.turnOnOrOffNotificationSetting();
				break;

			case R.id.voice_switch:
				presenter.turnOnOrOffVoiceSetting();
				break;
			default:
				break; 
		}
	}

	@Override
	public void initDeviceBanner(List<PeripheralInfo> deviceInfos)
	{
		bannerPagerView.setData(deviceInfos);
	}

	@Override
	public void turnOnNotificationSetting()
	{
		notificationSwitch.setChecked(true);
	}

	@Override
	public void turnOffNotificationSetting()
	{
		notificationSwitch.setChecked(false);
	}

	@Override
	public void turnOnVoiceSetting()
	{
		voiceSwitch.setChecked(true);
	}

	@Override
	public void turnOffVoiceSetting()
	{
		voiceSwitch.setChecked(false);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		presenter.unregisterFromBus();
	}
}
