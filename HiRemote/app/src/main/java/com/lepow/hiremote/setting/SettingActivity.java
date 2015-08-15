package com.lepow.hiremote.setting;

import android.os.Bundle;
import android.view.View;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by peng on 15/8/15.
 */
public class SettingActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.add_new_device, R.id.terms_service, R.id.privacy_policy})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_new_device:
                break;

            case R.id.terms_service:
                break;

            case R.id.privacy_policy:
                break;

            default:
                break;
        }
    }
}
