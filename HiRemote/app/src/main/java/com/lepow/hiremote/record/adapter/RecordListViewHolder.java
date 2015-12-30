package com.lepow.hiremote.record.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;
import com.mn.tiger.app.TGApplicationProxy;
import com.mn.tiger.utility.DateUtils;
import com.mn.tiger.widget.adpter.TGViewHolder;

import java.util.Locale;

import butterknife.Bind;


public class RecordListViewHolder extends TGViewHolder<RecordInfo>
{
	@Bind(R.id.record_name)
	TextView recordName;
	
	@Bind(R.id.record_date)
	TextView recordDate;

	@Bind(R.id.record_duration)
	TextView recordDuration;

	private Locale locale;

	@Override
	public View initView(ViewGroup parent, int viewType)
	{
		locale = TGApplicationProxy.getInstance().getApplication().getResources().getConfiguration().locale;
		return super.initView(parent, viewType);
	}

	@Override
	public void fillData(ViewGroup parent, View convertView, RecordInfo itemData, int position, int viewType)
	{
		recordName.setText(itemData.getTitle());

		String date = DateUtils.date2String(itemData.getTimestamp(),"yyyy-MM-dd HH:mm:ss");
		if(!locale.getLanguage().equalsIgnoreCase("zh") && !locale.equals(Locale.KOREA) && !locale.equals(Locale.KOREAN))
		{
			date = DateUtils.date2String(itemData.getTimestamp(), "MM-dd-yyyy HH:mm:ss");
		}

		recordDate.setText(date);
	}
}
