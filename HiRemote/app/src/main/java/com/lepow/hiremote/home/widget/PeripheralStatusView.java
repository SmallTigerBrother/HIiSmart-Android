package com.lepow.hiremote.home.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.widget.CircleProgressBar;
import com.mn.tiger.widget.imageview.CircleImageView;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by Dalang on 2015/8/21.
 */
public class PeripheralStatusView extends LinearLayout
{
    @FindView(R.id.peripheral_power)
    CircleProgressBar powerlProgressBar;

    @FindView(R.id.peripheral_image)
    CircleImageView peripheralImageView;

    @FindView(R.id.peripheral_location)
    TextView peripheralLocationView;

    @FindView(R.id.peripheral_name)
    TextView peripheralNameView;

    private PeripheralInfo peripheralInfo;

    public PeripheralStatusView(Context context)
    {
        super(context);
        init();
    }

    public PeripheralStatusView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        View mainView = LayoutInflater.from(getContext()).inflate(R.layout.device_banner_item, null);
        ButterKnife.bind(this, mainView);
        this.addView(mainView);
    }

    public void setData(PeripheralInfo peripheralInfo)
    {
        this.peripheralInfo = peripheralInfo;

        peripheralNameView.setText(peripheralInfo.getPeripheralName());
        powerlProgressBar.setProgress(peripheralInfo.getEnergy());
        //TODO 设置电量
    }

    public void setPeripheralEnergy(int energy)
    {
        this.peripheralInfo.setEnergy(energy);
        //TODO 设置电量
    }
}
