package com.lepow.hiremote.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.app.WebViewActivity;
import com.lepow.hiremote.bluetooth.ScanNewPeripheralActivity;
import com.lepow.hiremote.misc.IntentKeys;
import com.lepow.hiremote.misc.ServerUrls;

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

    @OnClick({R.id.add_new_peripheral, R.id.terms_service, R.id.privacy_policy})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_new_peripheral:
                startActivity(new Intent(this, ScanNewPeripheralActivity.class));
                break;

            case R.id.terms_service:
                startWebViewActivity(getString(R.string.terms_service), ServerUrls.TERMS_SERVICE);
                break;

            case R.id.privacy_policy:
                startWebViewActivity(getString(R.string.privacy_policy), ServerUrls.PRIVACY_POLICY);
                break;

            default:
                break;
        }
    }

    private void startWebViewActivity(String title, String url)
    {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(IntentKeys.WEBVIEW_ACTIVITY_TITLE, title);
        intent.putExtra(IntentKeys.URL, url);
        startActivity(intent);
    }
}
