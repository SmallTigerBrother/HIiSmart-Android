package com.lepow.hiremote.connect;

import android.os.Bundle;

import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.connect.present.AddDevicePresenter;
import com.lepow.hiremote.connect.present.IAddDeviceView;

public class ScanDeviceActivity extends BaseActivity implements IAddDeviceView
{
	private AddDevicePresenter presenter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		presenter = new AddDevicePresenter(this, this);
	}
}
