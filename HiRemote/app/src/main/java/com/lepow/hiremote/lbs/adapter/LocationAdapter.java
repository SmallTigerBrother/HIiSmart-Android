package com.lepow.hiremote.lbs.adapter;

import android.content.Context;

import com.lepow.hiremote.R;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.widget.recyclerview.TGRecyclerViewAdapter;
import com.mn.tiger.widget.recyclerview.TGRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;

/**
 * Created by peng on 15/9/19.
 */
public class LocationAdapter extends TGRecyclerViewAdapter<LocationInfo> implements BGASwipeItemLayout.BGASwipeItemLayoutDelegate
{
    private ArrayList<BGASwipeItemLayout> openedItems = new ArrayList<BGASwipeItemLayout>();

    public LocationAdapter(Context context, List<LocationInfo> items)
    {
        super(context, items, R.layout.location_list_item, LocationRecyclerViewHolder.class);
    }

    @Override
    public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout bgaSwipeItemLayout)
    {
        closeOpenedItems();
        openedItems.add(bgaSwipeItemLayout);
    }

    @Override
    public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout bgaSwipeItemLayout)
    {
        openedItems.remove(bgaSwipeItemLayout);
    }

    @Override
    public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout bgaSwipeItemLayout)
    {
        closeOpenedItems();
    }

    public void closeOpenedItems()
    {
        for (BGASwipeItemLayout item : openedItems)
        {
            item.closeWithAnim();
        }

        openedItems.clear();
    }
}
