package com.lepow.hiremote.lbs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.bluetooth.HSBLEPeripheralManager;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;
import com.lepow.hiremote.lbs.api.AMapManager;
import com.lepow.hiremote.lbs.api.IMapManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.ActivityResultCode;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.imageview.CircleImageView;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;

/**
 * Created by peng on 15/7/21.
 */
public class FindMyItemActivity extends BaseActivity implements View.OnClickListener
{
    @FindView(R.id.device_avatar)
    CircleImageView deviceAvatarView;

    @FindView(R.id.peripheral_name)
    TextView deviceName;

    @FindView(R.id.device_connect_status)
    ImageView connectStatusView;

    @FindView(R.id.device_location)
    TextView deviceLocationView;

    @FindView(R.id.mapview_container)
    FrameLayout mapContainer;

    private PeripheralInfo deviceInfo;

    private LocationInfo locationInfo;

    private IMapManager mapManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_my_item_layout);
        ButterKnife.bind(this);
        setBarTitleText(getString(R.string.find_my_item_title));
        showRightBarButton(true);
        getRightBarButton().setImageResource(R.drawable.add_device);
        getRightBarButton().setOnClickListener(this);

        deviceInfo = (PeripheralInfo)getIntent().getSerializableExtra(IntentKeys.PERIPHERAL_INFO);

//        deviceAvatarView.setImageResource(CR.getDrawableId(this, deviceInfo.getPeripheralImage()));
//        deviceName.setText(deviceInfo.getPeripheralName());
//        deviceLocationView.setText(deviceInfo.getLocation().getAddress());

//        if(deviceInfo.getState() == TGBluetoothManager.ConnectState.Connected)
//        {
//
//        }
//        else
//        {
//
//        }

        locationInfo = (LocationInfo)getIntent().getSerializableExtra(IntentKeys.LOCATION_INFO);

        initMapView(savedInstanceState, locationInfo);
    }

    private void initMapView(Bundle savedInstanceState, LocationInfo locationInfo)
    {
        mapManager = new AMapManager(this);
        mapManager.init(mapContainer, savedInstanceState);

        if(null != locationInfo)
        {
            mapManager.addMarker(Double.valueOf(locationInfo.getLatitude()), Double.valueOf(locationInfo.getLongitude()),
                    locationInfo.getAddress());
        }
    }

    @Override
    protected void initNavigationResource(TGNavigationBar navigationBar)
    {
        super.initNavigationResource(navigationBar);
        navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
    }

    @OnClick({R.id.buzz_my_item, R.id.stop_buzz})
    public void onClick(View view)
    {
        if(view == getRightBarButton())
        {
            Intent intent = new Intent(this, DisconnectLocationHistory.class);
            startActivityForResult(intent, 0);
            return;
        }

        switch (view.getId())
        {
            case R.id.buzz_my_item:
                HSBLEPeripheralManager.getInstance().turnOnAlarmImmediately();
//                HSBLEPeripheralManager.getInstance().readAndListenPower();
                break;

            case R.id.stop_buzz:
                HSBLEPeripheralManager.getInstance().turnOffAlarmImmediately();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapManager.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapManager.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapManager.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO 刷新界面位置
        if(resultCode == ActivityResultCode.DISCONNECT_LOCATION_HISTORY)
        {
            LocationInfo locationInfo = (LocationInfo)data.getSerializableExtra(IntentKeys.LOCATION_INFO);
        }
    }
}
