package com.lepow.hiremote.lbs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.adapter.LocationViewHolder;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.ActivityResultCode;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.misc.IntentKeys;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.adpter.TGListAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.tiger.OnQueryText;

public class PinnedLocationHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
	@FindView(R.id.location_search)
	TGSearchView searchView;
	
	@FindView(R.id.pinned_location_history)
	SwipeMenuListView listView;
	
	private TGListAdapter<LocationInfo> listAdapter;

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			listAdapter.updateData(LocationDataManager.getInstance().findAllPinnedLocationSortByTime(PinnedLocationHistoryActivity.this));
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_history);
		setBarTitleText(getString(R.string.location_history));
		ButterKnife.bind(this);
		this.registerReceiver(broadcastReceiver, new IntentFilter(IntentAction.ACTION_PINNED_LOCATION));

		searchView.setQueryTextColor(getResources().getColor(R.color.text_color_normal));

		listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
		listView.setMenuCreator(new SwipeMenuCreator()
		{
			@Override
			public void create(SwipeMenu swipeMenu)
			{
				SwipeMenuItem editItem = new SwipeMenuItem(PinnedLocationHistoryActivity.this);
				editItem.setIcon(R.drawable.location_edit);
				editItem.setBackground(new ColorDrawable(getResources().getColor(R.color.color_val_d9d9d9)));
				editItem.setWidth(getResources().getDimensionPixelSize(R.dimen.margin_val_120px));
				swipeMenu.addMenuItem(editItem);

				SwipeMenuItem deleteItem = new SwipeMenuItem(PinnedLocationHistoryActivity.this);
				deleteItem.setIcon(R.drawable.location_trash);
				deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.color_val_d9d9d9)));
				deleteItem.setWidth(getResources().getDimensionPixelSize(R.dimen.margin_val_120px));
				swipeMenu.addMenuItem(deleteItem);
			}
		});

		listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index)
			{
				switch (index)
				{
					case 0:
						openEditLocationDialog((LocationInfo)listAdapter.getItem(position));
						return true;

					case 1:
						openDeleteConfirmDialog((LocationInfo) listAdapter.getItem(position));
						return true;

					default:
						return false;
				}
			}
		});

		listAdapter = new TGListAdapter<LocationInfo>(this,
				LocationDataManager.getInstance().findAllPinnedLocationSortByTime(this),
				R.layout.location_list_item, LocationViewHolder.class);
		listAdapter.setStrictlyReuse(true);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(this, PinnedLocationMapActivity.class);
		intent.putExtra(IntentKeys.LOCATION_INFO, (LocationInfo)listAdapter.getItem(position));
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

	private void openDeleteConfirmDialog(final LocationInfo locationInfo)
	{
		HSAlertDialog dialog = new HSAlertDialog(this);
		dialog.setTitleVisibility(View.GONE);
		dialog.setBodyText(getString(R.string.make_sure_you_delete_location));
		//设置取消按钮
		dialog.setLeftButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		//设置确定按钮
		dialog.setRightButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				removeLocation(locationInfo);
				//更新数据库
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * 删除一条定位记录
	 * @param itemData
	 */
	private void removeLocation(LocationInfo itemData)
	{
		switch (itemData.getDataType())
		{
			case LocationInfo.DATA_TYPE_PINNED_LOCATION:
				LocationDataManager.getInstance().removePinnedLocation(this, itemData);
				break;

			case LocationInfo.DATA_TYPE_DISCONNECT_LOCATION:
				LocationDataManager.getInstance().removeDisconnectedLocation(this, itemData);
				break;

			default:
				break;
		}

		listAdapter.removeItem(itemData);
	}

	/**
	 *启动地图界面
	 * @param dataType
	 */
	private void startMapActivity(int dataType, LocationInfo locationInfo)
	{
		Intent intent = new Intent();
		switch (dataType)
		{
			case LocationInfo.DATA_TYPE_PINNED_LOCATION:
				//启动地图界面
				intent.setClass(this, PinnedLocationMapActivity.class);
				this.startActivity(intent);
				break;

			case LocationInfo.DATA_TYPE_DISCONNECT_LOCATION:
				//返回到上一界面
				intent.putExtra(IntentKeys.LOCATION_INFO, locationInfo);
				this.setResult(ActivityResultCode.DISCONNECT_LOCATION_HISTORY, intent);
				this.finish();

				break;
			default:
				break;
		}
	}

	/**
	 * 打开编辑对话框
	 */
	private void openEditLocationDialog(final LocationInfo locationInfo)
	{
		HSAlertDialog dialog = new HSAlertDialog(this);
		dialog.setTitleText(getString(R.string.edit_location_title));
		//设置输入框
		final EditText editText = new EditText(this);
		editText.setBackgroundColor(Color.TRANSPARENT);
		editText.setTextColor(getResources().getColor(R.color.text_color_normal));
		String address = TextUtils.isEmpty(locationInfo.getRemark()) ? locationInfo.getAddress() : locationInfo.getRemark();
		editText.setText(address);
		editText.setSelection(address.length());
		editText.setSelection(locationInfo.getAddress().length());
		ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		editText.setPadding(getResources().getDimensionPixelSize(R.dimen.margin_val_20px),
				getResources().getDimensionPixelSize(R.dimen.margin_val_20px),
				getResources().getDimensionPixelSize(R.dimen.margin_val_20px),
				getResources().getDimensionPixelSize(R.dimen.margin_val_30px));
		dialog.setBodyContentView(editText, layoutParams);
		//设置取消按钮
		dialog.setLeftButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				dialog.dismiss();
			}
		});

		//设置确定按钮
		dialog.setRightButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//更新数据库
				locationInfo.setRemark(editText.getText().toString().trim());
				switch (locationInfo.getDataType())
				{
					case LocationInfo.DATA_TYPE_PINNED_LOCATION:
						LocationDataManager.getInstance().savePinnedLocation(PinnedLocationHistoryActivity.this, locationInfo);
						break;

					case LocationInfo.DATA_TYPE_DISCONNECT_LOCATION:
						LocationDataManager.getInstance().saveDisconnectedLocation(PinnedLocationHistoryActivity.this, locationInfo);
						break;
					default:
						break;
				}
				//更新列表
				listAdapter.notifyDataSetChanged();
				((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		this.unregisterReceiver(broadcastReceiver);
	}
}
