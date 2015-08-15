package com.lepow.hiremote.lbs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.connect.data.DeviceInfo;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.bluetooth.TGBluetoothManager;
import com.mn.tiger.bluetooth.event.Connect2DeviceEvent;
import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.imageview.CircleImageView;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by peng on 15/7/21.
 */
public class FindMyItemActivity extends BaseActivity
{
    @FindView(R.id.device_avatar)
    CircleImageView deviceAvatarView;

    @FindView(R.id.device_name)
    TextView deviceName;

    @FindView(R.id.device_connect_status)
    ImageView connectStatusView;

    @FindView(R.id.device_location)
    TextView deviceLocationView;

    @FindView(R.id.buzz_my_item)
    Button buzzBtn;

    @FindView(R.id.stop_buzz)
    Button stopBuzzBtn;

    @FindView(R.id.mapview_container)
    FrameLayout mapContainer;

    private DeviceInfo deviceInfo;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_my_item_layout);
        ButterKnife.bind(this);
        deviceInfo = (DeviceInfo)getIntent().getSerializableExtra(IntentKeys.DEVICE_INFO);

        deviceAvatarView.setImageResource(CR.getDrawableId(this,deviceInfo.getDeviceImage()));
        deviceName.setText(deviceInfo.getDeviceName());
        deviceLocationView.setText(deviceInfo.getLocation().getAddress());

        if(deviceInfo.getState() == TGBluetoothManager.ConnectState.Success)
        {

        }
        else
        {

        }

        initMapView();
    }

    private void initMapView()
    {

    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buzz_my_item:
                break;

            case R.id.stop_buzz:
                break;

            default:
                break;
        }
    }

    @Subscribe
    public void onConnectDevice(Connect2DeviceEvent event)
    {
        switch (event.getState())
        {
            case Success:
                break;

            case Failed:
                break;

            default:
                break;
        }
    }

}
