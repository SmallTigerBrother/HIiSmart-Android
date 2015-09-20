package com.lepow.hiremote.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;

/**
 * 设备扫描界面
 */
public class ScanPeripheralActivity extends BaseActivity
{
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
		public void onReceive(final Context context, Intent intent)
		{
			int bleState = TGBLEManager.getBLEState(intent);
			PeripheralInfo peripheralInfo = PeripheralInfo.fromBLEPeripheralInfo(TGBLEManager.getBLEPeripheralInfo(intent));
			switch (bleState)
			{
				case TGBLEManager.BLE_STATE_CONNECTED:
					onPeripheralConnected(peripheralInfo);
					break;
				case TGBLEManager.BLE_STATE_NO_PERIPHERAL_FOUND:
					onNoPeripheralFound();
					break;

				case TGBLEManager.BLE_STATE_POWER_OFF:
					onBlueToothPowerOff();
					break;

				case TGBLEManager.BLE_STATE_NONSUPPORT:
					onBLENonSupport();
					break;
				case TGBLEManager.BLE_STATE_POWER_ON:
					onBlueToothPowerOn();
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
				onScanRetry();
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

	protected void onScanRetry()
	{
		scanDevice();
	}

	protected void onPeripheralConnected(final PeripheralInfo peripheralInfo)
	{
		connectSuccessLayout.setVisibility(View.VISIBLE);
		scanningLayout.setVisibility(View.GONE);
		peripheralInfo.setConnected(true);
		PeripheralDataManager.savePeripheral(ScanPeripheralActivity.this, peripheralInfo);
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				gotoRenamePeripheralActivity(peripheralInfo);
				finish();
			}
		}, 1000);
	}

	protected void onNoPeripheralFound()
	{
		scanningLayout.setVisibility(View.GONE);
		notFoundPeripheralLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 蓝牙关闭时的回调方法
	 */
	protected void onBlueToothPowerOff()
	{
		HSBLEPeripheralManager.getInstance().showBluetoothOffDialog(ScanPeripheralActivity.this);
	}

	/**
	 * 不支持蓝牙4.0时的回调方法
	 */
	protected void onBLENonSupport()
	{
		HSBLEPeripheralManager.getInstance().showNonSupportBLEDialog(ScanPeripheralActivity.this);
	}

	/**
	 * 蓝牙打开回调方法
	 */
	protected void onBlueToothPowerOn()
	{
		scanningLayout.setVisibility(View.VISIBLE);
		notFoundPeripheralLayout.setVisibility(View.GONE);
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


}
