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

}
