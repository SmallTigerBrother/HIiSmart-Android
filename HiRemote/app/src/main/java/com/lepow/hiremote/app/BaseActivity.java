package com.lepow.hiremote.app;

import android.app.DialogFragment;
import android.view.View;

import com.lepow.hiremote.R;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.widget.TGNavigationBar;

public class BaseActivity extends TGActionBarActivity
{
	@Override
	protected DialogFragment initLoadingDialog()
	{
		return super.initLoadingDialog();
	}
	
	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
		navigationBar.getLeftNaviButton().setImageResource(R.drawable.add_device);
		navigationBar.getMiddleTextView().setTextColor(getResources().getColor(R.color.text_color_normal));
		navigationBar.getLeftNaviButton().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
	}
}
