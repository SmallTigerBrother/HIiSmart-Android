package com.lepow.hiremote.record.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.FindView;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.record.data.RecordTimeInfo;

public class RecordListViewHolder extends TGViewHolder<Object>
{
	@FindView(R.id.record_name)
	TextView recordName;
	
	@FindView(R.id.record_date)
	TextView recordDate;
	
	@FindView(R.id.record_duration)
	TextView recordTimeLength;
	
	@SuppressLint("InflateParams")
	@Override
	public View initView(View convertView, ViewGroup parent, int position)
	{
		switch (getAdapter().getItemViewType(position))
		{
			case RecordListAdapter.TYPE_RECORD:
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_history_list_item, null);
				
				ButterKnife.bind(this, convertView);
				
				return convertView;
				
			case RecordListAdapter.TYPE_TIME:
				
				TextView textView = new TextView(getContext());
				
				return textView;

			default:
				return null;
		}
	}
	
	@Override
	public void fillData(ViewGroup parent, View convertView, Object itemData, int position)
	{
		switch (getAdapter().getItemViewType(position))
		{
			case RecordListAdapter.TYPE_RECORD:
				recordName.setText(((RecordInfo)itemData).getName());
				recordTimeLength.setText(((RecordInfo)itemData).getDuration() + "");
				recordDate.setText(((RecordInfo)itemData).getDateString() + "");
				break;
				
			case RecordListAdapter.TYPE_TIME:
				((TextView)convertView).setText(((RecordTimeInfo)itemData).getTimeString());
				break;

			default:
				break;
		}
	}

}
