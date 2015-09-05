package com.lepow.hiremote.record.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.utils.DurationUtils;
import com.mn.tiger.utility.DateUtils;
import com.mn.tiger.widget.adpter.TGViewHolder;

import butterknife.FindView;

public class RecordListViewHolder extends TGViewHolder<RecordInfo>
{
	@FindView(R.id.record_name)
	TextView recordName;
	
	@FindView(R.id.record_date)
	TextView recordDate;

	@FindView(R.id.record_duration)
	TextView recordDuration;
	
	@Override
	public void fillData(ViewGroup parent, View convertView, RecordInfo itemData, int position)
	{
		recordName.setText(itemData.getTitle());
		recordDate.setText(DateUtils.date2String(itemData.getTimestamp(), DateUtils.DATE_FORMAT));
		recordDuration.setText(DurationUtils.duration2String(itemData.getDuration()));
	}
}
