package com.lepow.hiremote.record.present;

import android.app.Activity;

import com.lepow.hiremote.app.Presenter;
import com.lepow.hiremote.record.data.RecordManager;

public class RecordListItemPresenter extends Presenter
{
	private IRecordListItemView view;
	
	public RecordListItemPresenter(Activity activity, IRecordListItemView view)
	{
		super(activity);
		this.view = view;
	}
	
	public void removeRecord()
	{
		RecordManager.getInstanse().removeRecord(getActivity(), view.getRecordInfo());
		view.removeSelf();
	}

	public void playRecord()
	{
		
	}
	
}
