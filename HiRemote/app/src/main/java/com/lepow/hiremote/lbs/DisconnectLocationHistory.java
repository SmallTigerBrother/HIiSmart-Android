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
import com.lepow.hiremote.misc.ActivityResultCode;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.recyclerview.TGRecyclerView;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * 断开连接的地址历史界面
 */
public class DisconnectLocationHistory extends BaseActivity implements TGRecyclerView.OnItemClickListener
{
    @FindView(R.id.disconnect_location_history)
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
                listAdapter.updateData(LocationDataManager.getInstance().findAllDisconnectedLocationOderByTime(
                        DisconnectLocationHistory.this));
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disconnect_location_history);
        setBarTitleText(getString(R.string.disconnection_history));
        ButterKnife.bind(this);

        this.registerReceiver(broadcastReceiver, new IntentFilter(IntentAction.ACTION_DISCONNECTED_LOCATION));

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

        listAdapter = new LocationAdapter(this, LocationDataManager.getInstance().findAllDisconnectedLocationOderByTime(this));
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
    public void onItemClick(RecyclerView recyclerView, View view, int i, long l)
    {
        Intent data = new Intent();
        data.putExtra(IntentKeys.LOCATION_INFO, listAdapter.getItem(i));
        this.setResult(ActivityResultCode.DISCONNECT_LOCATION_HISTORY, data);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(broadcastReceiver);
    }
}
