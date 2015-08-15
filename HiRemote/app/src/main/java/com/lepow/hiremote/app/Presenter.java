package com.lepow.hiremote.app;

import android.app.Activity;

import com.mn.tiger.app.AbstractPresenter;

public class Presenter extends AbstractPresenter
{
	public Presenter(Activity activity)
	{
		super(activity);
	}
	
	public boolean hasAuthorised()
	{
		return false;
	}

	public void register2Bus()
	{
		HSApplication.getBus().register(this);
	}

	public void unregisterFromBus()
	{
		HSApplication.getBus().unregister(this);
	}
}
