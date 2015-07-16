package com.lepow.hiremote.lbs.present;

import android.app.Activity;

import com.mn.tiger.app.AbstractPresenter;

public class LocationHistoryPresenter extends AbstractPresenter
{
	private ILocationHistoryView view;
	
	public LocationHistoryPresenter(Activity activity, ILocationHistoryView view)
	{
		super(activity);
		this.view = view;
	}

}
