package com.lepow.hiremote.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.app.WebViewActivity;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.home.HomeActivity;
import com.lepow.hiremote.misc.IntentKeys;
import com.lepow.hiremote.misc.ServerUrls;
import com.mn.tiger.bluetooth.event.ConnectPeripheralEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;

/**
 * 设备扫描界面
 */
public class ScanPeripheralActivity extends BaseActivity
{
	@FindView(R.id.scan_device_cancel_btn)
	Button cancelBtn;

	@FindView(R.id.scan_device_retry_btn)
	Button retryBtn;

	@FindView(R.id.scaning_layout)
	RelativeLayout scanningLayout;

	@FindView(R.id.connect_success_layout)
	LinearLayout connectSuccessLayout;

	@FindView(R.id.not_found_peripheral_layout)
	RelativeLayout notFoundPeripheralLayout;

	protected static Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_device_activity);
		ButterKnife.bind(this);

		HSApplication.getBus().register(this);

		//开始扫描设备
		scanDevice();
	}

	protected void scanDevice()
	{
		scanningLayout.setVisibility(View.VISIBLE);
		notFoundPeripheralLayout.setVisibility(View.GONE);

	}

	@OnClick({R.id.scan_device_cancel_btn, R.id.scan_device_retry_btn})
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.scan_device_cancel_btn:
				gotoHomeActivity(null);
				break;

			case R.id.scan_device_retry_btn:
				scanDevice();
				break;

			case R.id.not_found_peripheral_help:
				Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(IntentKeys.WEBVIEW_ACTIVITY_TITLE, getString(R.string.support));
				intent.putExtra(IntentKeys.URL, ServerUrls.SUPPORT_FAQ);
				startActivity(intent);
				break;

			default:
				break;
		}
	}

	@Subscribe
	public void onConnectDevice(final ConnectPeripheralEvent event)
	{
		switch (event.getState())
		{
			case Connected:
				connectSuccessLayout.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						gotoRenamePeripheralActivity(event.getPeripheralInfo());
						finish();
					}
				}, 2000);
				break;

			case Disconnect:
				scanningLayout.setVisibility(View.GONE);
				cancelBtn.setVisibility(View.VISIBLE);
				retryBtn.setVisibility(View.VISIBLE);
				break;

			case BluetoothOff:
				HSBLEPeripheralManager.getInstance().showBluetoothOffDialog(this);
				break;

			case Nonsupport:
				HSBLEPeripheralManager.getInstance().showNonSupportBLEDialog(this);
				break;

			default:
				break;
		}
	}

	/**
	 * 进入重命名设备界面
	 * @param peripheralInfo
	 */
	private void gotoRenamePeripheralActivity(PeripheralInfo peripheralInfo)
	{
		Intent intent = new Intent(this, RenamePeripheralActivity.class);
		intent.putExtra(IntentKeys.PERIPHERAL_INFO, peripheralInfo);
		startActivity(intent);
	}

	/**
	 * 进入主界面
	 * @param peripheralInfo
	 */
	protected void gotoHomeActivity(PeripheralInfo peripheralInfo)
	{
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.PERIPHERAL_INFO, peripheralInfo);
		startActivity(intent);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		HSApplication.getBus().unregister(this);
		//界面被销毁时，停止扫描蓝牙设备
		HSBLEPeripheralManager.getInstance().stopScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		HSBLEPeripheralManager.getInstance().onActivityResult(requestCode,resultCode,data);
	}

}
