package com.lepow.hiremote.record.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.utils.DurationUtils;
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

		String date = DateUtils.date2String(itemData.getTimestamp(),DateUtils.DATE_FORMAT);
		if(!locale.getLanguage().equalsIgnoreCase("zh"))
		{
			date = DateUtils.date2String(itemData.getTimestamp(), "MM-dd-yyyy");
		}

		recordDate.setText(date);
		recordDuration.setText(DurationUtils.duration2String(itemData.getDuration()));
	}
}
