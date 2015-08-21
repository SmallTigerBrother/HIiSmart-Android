package com.lepow.hiremote.lbs;

import android.os.Bundle;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.adapter.LocationViewHolder;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.swipelistview.SwipeListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.tiger.OnQueryText;

public class PinnedLocationHistoryActivity extends BaseActivity
{
	@FindView(R.id.location_search)
	TGSearchView searchView;
	
	@FindView(R.id.pinned_location_history)
	SwipeListView listView;
	
	private TGListAdapter<LocationInfo> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_history);
		
		ButterKnife.bind(this);

		listView.setSwipeCloseAllItemsWhenMoveList(true);
		listView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		listView.setSwipeOpenOnLongPress(true);

		listAdapter = new TGListAdapter<LocationInfo>(this, null,
				R.layout.location_list_item, LocationViewHolder.class);
		listView.setAdapter(listAdapter);
//		listAdapter.updateData(LocationDataManager.getInstance().findAllPinnedLocationSortByTime(this));
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
	}

	/**
	 * 搜索提交回调方法
	 * @param queryText
	 */
	@OnQueryText(R.id.location_search)
	public void onSearchSubmit(CharSequence queryText)
	{
		//根据关键字查找本地数据
		List<LocationInfo> results = LocationDataManager.getInstance().findAllPinnedLocationSortByTime(this, queryText.toString());
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
