package com.lepow.hiremote.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.app.WebViewActivity;
import com.lepow.hiremote.bluetooth.data.PeripheralDataManager;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.home.HomeActivity;
import com.lepow.hiremote.misc.IntentKeys;
import com.lepow.hiremote.misc.ServerUrls;
import com.mn.tiger.bluetooth.TGBLEManager;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;

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

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int bleState = TGBLEManager.getBLEState(intent);
			final TGBLEPeripheralInfo peripheralInfo = TGBLEManager.getBLEPeripheralInfo(intent);
			switch (bleState)
			{
				case TGBLEManager.BLE_STATE_CONNECTED:
					connectSuccessLayout.setVisibility(View.VISIBLE);
					scanningLayout.setVisibility(View.GONE);
					PeripheralDataManager.savePeripheral(ScanPeripheralActivity.this, PeripheralInfo.fromBLEPeripheralInfo(peripheralInfo));
					handler.postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							gotoRenamePeripheralActivity(PeripheralInfo.fromBLEPeripheralInfo(peripheralInfo));
							finish();
						}
					}, 1000);
					break;
				case TGBLEManager.BLE_STATE_DISCONNECTED:
					scanningLayout.setVisibility(View.GONE);
					notFoundPeripheralLayout.setVisibility(View.VISIBLE);
					break;

				case TGBLEManager.BLE_STATE_NONSUPPORT:
					HSBLEPeripheralManager.getInstance().showBluetoothOffDialog(ScanPeripheralActivity.this);
					break;

				case TGBLEManager.BLE_STATE_POWER_OFF:
					HSBLEPeripheralManager.getInstance().showNonSupportBLEDialog(ScanPeripheralActivity.this);
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_device_activity);
		ButterKnife.bind(this);
		getWindow().getDecorView().setBackgroundResource(R.color.default_green_bg);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TGBLEManager.ACTION_BLE_STATE_CHANGE);
		this.registerReceiver(broadcastReceiver, intentFilter);
		//开始扫描设备

		scanningLayout.setVisibility(View.VISIBLE);
		notFoundPeripheralLayout.setVisibility(View.GONE);
		scanDevice();
	}

	protected void scanDevice()
	{
		HSBLEPeripheralManager.getInstance().scanAndConnect2Peripheral();
	}

	@OnClick({R.id.scan_device_cancel_btn, R.id.scan_device_retry_btn, R.id.not_found_peripheral_help})
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.scan_device_cancel_btn:
				gotoHomeActivity(null);
				break;

			case R.id.scan_device_retry_btn:
				scanningLayout.setVisibility(View.VISIBLE);
				notFoundPeripheralLayout.setVisibility(View.GONE);
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
		//界面被销毁时，停止扫描蓝牙设备
		HSBLEPeripheralManager.getInstance().stopScan();
		this.unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	}

}
