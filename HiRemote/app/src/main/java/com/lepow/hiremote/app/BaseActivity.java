package com.lepow.hiremote.app;

import android.app.DialogFragment;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.widget.TGNavigationBar;

public class BaseActivity extends TGActionBarActivity
{
	@Override
	protected DialogFragment initLoadingDialog()
	{
		return super.initLoadingDialog();
	}
	
	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
	}
}
