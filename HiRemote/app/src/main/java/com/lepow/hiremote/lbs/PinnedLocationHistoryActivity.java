package com.lepow.hiremote.lbs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.adapter.LocationAdapter;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.recyclerview.TGRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.tiger.OnQueryText;

public class PinnedLocationHistoryActivity extends BaseActivity implements
		TGRecyclerView.OnItemClickListener
{
	@FindView(R.id.location_search)
	TGSearchView searchView;
	
	@FindView(R.id.pinned_location_history)
	TGRecyclerView listView;
	
	private LocationAdapter listAdapter;

	private BroadcastReceiver broadcastReceiver;

	{
		broadcastReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				listAdapter.closeOpenedItems();
				listAdapter.updateData(LocationDataManager.getInstance().findAllPinnedLocationSortByTime(PinnedLocationHistoryActivity.this));
			}
		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_history);
		setBarTitleText(getString(R.string.location_history));
		ButterKnife.bind(this);
		this.registerReceiver(broadcastReceiver, new IntentFilter(IntentAction.ACTION_PINNED_LOCATION));

		searchView.setQueryTextColor(getResources().getColor(R.color.text_color_normal));

		listView.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState)
			{
				if(RecyclerView.SCROLL_STATE_DRAGGING == newState)
				{
					listAdapter.closeOpenedItems();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}
		});

		listAdapter = new LocationAdapter(this,
				LocationDataManager.getInstance().findAllPinnedLocationSortByTime(this));
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(this);
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
	}

	@Override
	public void onItemClick(RecyclerView recyclerView, View view, int position, long id)
	{
		Intent intent = new Intent(this, PinnedLocationMapActivity.class);
		intent.putExtra(IntentKeys.LOCATION_INFO, listAdapter.getItem(position));
		startActivity(intent);
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
			listAdapter.updateData(results);
		}
		else
		{
			//更新列表内容
			listAdapter.updateData(results);
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		this.unregisterReceiver(broadcastReceiver);
	}
}
