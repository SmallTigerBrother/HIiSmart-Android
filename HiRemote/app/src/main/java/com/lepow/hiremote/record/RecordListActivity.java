package com.lepow.hiremote.record;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.FindView;

import com.lepow.hiremote.record.data.RecordInfo;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.swipelistview.BaseSwipeListViewListener;
import com.mn.tiger.widget.swipelistview.SwipeListView;
import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.record.adapter.RecordListViewHolder;
import com.lepow.hiremote.record.data.RecordManager;

public class RecordListActivity extends BaseActivity
{
	@FindView(R.id.record_list_view)
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_history_list_activity);
		
		ButterKnife.bind(this);
		
		listView.setAdapter(new TGListAdapter<RecordInfo>(this, RecordManager.getInstanse().findAllRecordsSortByTime(this),
				R.layout.record_history_list_item, RecordListViewHolder.class));
	}
	
	
	
}
