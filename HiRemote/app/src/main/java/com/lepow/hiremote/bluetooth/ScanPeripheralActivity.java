package com.lepow.hiremote.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.home.HomeActivity;
import com.lepow.hiremote.misc.IntentKeys;
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
	@FindView(R.id.scan_device_progress)
	ProgressBar scanProgressBar;

	@FindView(R.id.scan_device_cancel_btn)
	Button cancelBtn;

	@FindView(R.id.scan_device_retry_btn)
	Button retryBtn;

	@FindView(R.id.connect_success_layout)
	RelativeLayout connectSuccessLayout;

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
		scanProgressBar.setVisibility(View.VISIBLE);
		cancelBtn.setVisibility(View.GONE);
		retryBtn.setVisibility(View.GONE);
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
						gotoHomeActivity(event.getPeripheralInfo());
						finish();
					}
				}, 2000);
				break;

			case Disconnect:
				scanProgressBar.setVisibility(View.INVISIBLE);
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
	 * 进入主界面
	 * @param deviceInfo
	 */
	protected void gotoHomeActivity(PeripheralInfo deviceInfo)
	{
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(IntentKeys.DEVICE_INFO, deviceInfo);
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
