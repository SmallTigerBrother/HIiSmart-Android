package com.lepow.hiremote.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.app.WebViewActivity;
import com.lepow.hiremote.bluetooth.ScanNewPeripheralActivity;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.widget.TGNavigationBar;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by peng on 15/8/15.
 */
public class SettingActivity extends BaseActivity
{
    @Bind(R.id.add_new_peripheral)
    RelativeLayout addNewPeripheral;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        setBarTitleText(getString(R.string.settings));
        ButterKnife.bind(this);

        if(Locale.KOREA.equals(getResources().getConfiguration().locale) || Locale.KOREAN.equals(getResources().getConfiguration().locale))
        {
            addNewPeripheral.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initNavigationResource(TGNavigationBar navigationBar)
    {
        super.initNavigationResource(navigationBar);
        navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
    }

    @OnClick({R.id.add_new_peripheral, R.id.terms_conditions_layout, R.id.privacy_policy_layout, R.id.support_faq_layout, R.id.contact_us_layout})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_new_peripheral:
                startActivity(new Intent(this, ScanNewPeripheralActivity.class));
                finish();
                break;

            case R.id.terms_conditions_layout:
                startWebViewActivity(getString(R.string.terms_conditions), getString(R.string.terms_conditions_url));
                break;

            case R.id.privacy_policy_layout:
                startWebViewActivity(getString(R.string.privacy_policy), getString(R.string.privacy_url));
                break;

            case R.id.contact_us_layout:
                startWebViewActivity(getString(R.string.contact_us), getString(R.string.contact_url));
                break;

            case R.id.support_faq_layout:
                startWebViewActivity(getString(R.string.support_faq), getString(R.string.faq_url));
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
