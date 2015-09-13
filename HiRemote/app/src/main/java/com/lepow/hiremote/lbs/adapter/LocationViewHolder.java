package com.lepow.hiremote.lbs.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.widget.adpter.TGViewHolder;

import butterknife.FindView;

/**
 *定位记录列表行填充类
 */
public class LocationViewHolder extends TGViewHolder<LocationInfo>
{
    @FindView(R.id.location_date)
    TextView locationDate;

    @FindView(R.id.location_address)
    TextView locationAddress;

    @FindView(R.id.location_time)
    TextView locationTime;

    private LocationInfo itemData;

    @Override
    public void fillData(ViewGroup parent, View convertView, LocationInfo itemData, int position, int viewType)
    {
        this.itemData = itemData;

        locationDate.setText(((LocationInfo) itemData).getDateString());
        locationTime.setText(((LocationInfo) itemData).getTimeString());
        if(TextUtils.isEmpty(itemData.getRemark()))
        {
            locationAddress.setText(itemData.getAddress());
        }
        else
        {
            locationAddress.setText(itemData.getRemark());
        }
    }
}
