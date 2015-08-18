package com.lepow.hiremote.record;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.record.adapter.RecordListViewHolder;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.record.data.RecordDataManager;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.adpter.TGListAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnItemClick;
import butterknife.tiger.OnQueryText;

/**
 * 录音历史记录界面
 */
public class VoiceMemosActivity extends BaseActivity
{
	@FindView(R.id.record_search)
	TGSearchView searchView;

	@FindView(R.id.record_list_view)
	ListView listView;

	private TGListAdapter<RecordInfo> listAdapter;

	private RecordPopupWindow recordPopupWindow;
	
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

		recordPopupWindow = new RecordPopupWindow(this);
	}

	@OnItemClick(R.id.record_list_view)
	public void onItemClick(AdapterView<?> adapterView, View convertView, int position)
	{
		//显示录音播放、编辑框
		recordPopupWindow.setData((RecordInfo)listAdapter.getItem(position));
		recordPopupWindow.showAsDropDown(searchView);
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
}
