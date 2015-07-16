package com.lepow.hiremote.connect.present;

import android.app.Activity;
import com.lepow.hiremote.connect.present.IAddDeviceView;
import com.lepow.hiremote.app.Presenter;

public class AddDevicePresenter extends Presenter
{
	private IAddDeviceView view;

	public AddDevicePresenter(Activity activity, IAddDeviceView view)
	{
		super(activity);
		this.view = view;
	}

}
