package com.lepow.hiremote.lbs;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.tiger.OnQueryText;

import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.adapter.LocationViewHolder;
import com.lepow.hiremote.lbs.data.LocationManager;
import com.lepow.hiremote.lbs.present.ILocationHistoryView;

public class PinnedLocationHistoryActivity extends BaseActivity implements ILocationHistoryView
{
	@FindView(R.id.location_search)
	TGSearchView searchView;
	
	@FindView(R.id.pinned_location_history)
	ListView listView;
	
	private TGListAdapter<Object> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_history);
		
		ButterKnife.bind(this);
		
		listAdapter = new TGListAdapter<Object>(this, LocationManager.getInstanse().findAllPinnedLocationSortByTime(this), 
				-1, LocationViewHolder.class);
		listView.setAdapter(listAdapter);
	}
	
	/**
	 * 搜索提交回调方法
	 * @param queryText
	 */
	@OnQueryText(R.id.location_search)
	public void onSearchSubmit(CharSequence queryText)
	{
		//根据关键字查找本地数据
		List<Object> results = LocationManager.getInstanse().findAllPinnedLocationSortByTime(this, queryText.toString());
		if(null == results || results.size() == 0)
		{
			//TODO 显示未搜索到结果的提示界面
		}
		else
		{
			//更新列表内容
			listAdapter.updateData(results);
		}
	}
}
