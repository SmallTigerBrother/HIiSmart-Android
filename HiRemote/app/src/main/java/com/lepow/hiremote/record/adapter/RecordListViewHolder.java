package com.lepow.hiremote.record.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.FindView;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.lepow.hiremote.R;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.record.data.RecordTimeInfo;

public class RecordListViewHolder extends TGViewHolder<RecordInfo>
{
	@FindView(R.id.record_name)
	TextView recordName;
	
	@FindView(R.id.record_date)
	TextView recordDate;
	
	@FindView(R.id.record_duration)
	TextView recordTimeLength;
	
	@Override
	public void fillData(ViewGroup parent, View convertView, RecordInfo itemData, int position)
	{
		recordName.setText(((RecordInfo)itemData).getName());
		recordTimeLength.setText(((RecordInfo)itemData).getDuration() + "");
		recordDate.setText(((RecordInfo)itemData).getDateString() + "");
	}

}
