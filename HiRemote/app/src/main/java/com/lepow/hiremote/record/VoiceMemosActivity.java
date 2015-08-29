package com.lepow.hiremote.record;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.record.adapter.RecordListViewHolder;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.record.data.RecordDataManager;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnItemClick;
import butterknife.tiger.OnQueryText;

/**
 * 录音历史记录界面
 */
public class VoiceMemosActivity extends BaseActivity implements RecordEditDialog.OnRecordModifyListener
{
	@FindView(R.id.record_search)
	TGSearchView searchView;

	@FindView(R.id.record_list_view)
	ListView listView;

	private TGListAdapter<RecordInfo> listAdapter;

	private RecordEditDialog recordEditDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_history_list_activity);
		setBarTitleText(getString(R.string.voice_memo));
		ButterKnife.bind(this);

		listAdapter = new TGListAdapter<RecordInfo>(this, RecordDataManager.getInstance().findAllRecordsSortByTime(this),
			R.layout.record_history_list_item, RecordListViewHolder.class);
		listView.setAdapter(listAdapter);

		recordEditDialog = new RecordEditDialog(this);
		recordEditDialog.setOnRecordModifyListener(this);

		HSApplication.getBus().register(this);
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
	}

	@OnItemClick(R.id.record_list_view)
	public void onItemClick(AdapterView<?> adapterView, View convertView, int position)
	{
		//显示录音播放、编辑框
		recordEditDialog.setData((RecordInfo) listAdapter.getItem(position));
		recordEditDialog.show();
	}

	@OnQueryText(value = R.id.record_search, callback = OnQueryText.Callback.ON_QUERY_TEXT_SUBMIT)
	public void onSearchTextSubmit()
	{
		String queryText = searchView.getQueryText();
		if(!TextUtils.isEmpty(queryText))
		{
			List<RecordInfo> recordInfos = RecordDataManager.getInstance().findAllRecordsSortByTime(this, queryText);
			listAdapter.updateData(recordInfos);
		}
	}

	@Override
	public void onRecordModify(RecordInfo recordInfo)
	{
		List<RecordInfo> listItems = listAdapter.getListItems();
		int index = listItems.indexOf(recordInfo);
		listItems.set(index, recordInfo);
		listAdapter.updateData(listItems);
	}

	@Override
	public void onRecordDelete(RecordInfo recordInfo)
	{
		listAdapter.removeItem(recordInfo);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		HSApplication.getBus().unregister(this);
	}

	@Subscribe
	public void onNewRecord(RecordInfo recordInfo)
	{
		List<RecordInfo> listItems = listAdapter.getListItems();
		listItems.add(0, recordInfo);
		listAdapter.updateData(listItems);
	}
}
