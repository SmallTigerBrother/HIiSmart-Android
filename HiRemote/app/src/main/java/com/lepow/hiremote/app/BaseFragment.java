package com.lepow.hiremote.app;

import android.graphics.Color;
import android.view.View;

import com.lepow.hiremote.R;
import com.mn.tiger.app.TGFragment;
import com.mn.tiger.widget.TGNavigationBar;

public abstract class BaseFragment extends TGFragment
{
	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundColor(Color.WHITE);
		navigationBar.getLeftNaviButton().setImageResource(R.drawable.left_arrow);
		navigationBar.getLeftNaviButton().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().onBackPressed();
			}
		});
	}
}
