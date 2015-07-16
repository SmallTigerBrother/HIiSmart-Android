package com.lepow.hiremote.record.present;

import com.mn.tiger.app.IView;
import com.lepow.hiremote.record.data.RecordInfo;

public interface IRecordListItemView extends IView
{
	RecordInfo getRecordInfo();
	
	void removeSelf();
}
