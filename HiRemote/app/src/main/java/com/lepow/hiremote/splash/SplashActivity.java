package com.lepow.hiremote.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.home.HomeActivity;
import com.lepow.hiremote.splash.data.IntroductionInfo;
import com.mn.tiger.system.AppConfigs;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView.ViewPagerHolder;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity
{
	private int[] introTitleResIds = {R.string.intro_location_title, 
			R.string.intro_find_title,
			R.string.intro_camera_title,
			R.string.intro_voice_title,
			R.string.intro_hiremote_title};
	
	private int[] introImgResIds = {R.drawable.intro_1,
			R.drawable.intro_2,
			R.drawable.intro_3,
			R.drawable.intro_4,
			R.drawable.intro_5};
	
	private int[] introDetailResIds = {R.string.intro_location_detail,
			R.string.intro_find_detail,
			R.string.intro_camera_detail,
			R.string.intro_voice_detail, -1};
	
	@FindView(R.id.intro_viewpager)
	DotIndicatorBannerPagerView<IntroductionInfo> bannerPagerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setNavigationBarVisible(false);
		
		if(AppConfigs.isNewOrUpgrade(this))
		{
			setContentView(R.layout.splash_activity);
			
			ButterKnife.bind(this);

			bannerPagerView.setDotViewBackground(getResources().getDrawable(R.drawable.half_alpha_white_dot),
					getResources().getDrawable(R.drawable.white_dot));
			bannerPagerView.setCircleable(false);
			bannerPagerView.setData(initIntroductionInfos());
			bannerPagerView.setViewPagerHolder(new ViewPagerHolder<IntroductionInfo>()
			{
				@Override
				public void fillData(View pageOfView, IntroductionInfo itemData, 
						int position, int viewType)
				{
					TextView titleTextView = (TextView) pageOfView.findViewById(R.id.intro_title);
					titleTextView.setText(itemData.getIntroTitleResId());
					
					ImageView introImageView = (ImageView)pageOfView.findViewById(R.id.intro_image);
					introImageView.setImageResource(itemData.getIntroImgResId());
					
					TextView detailTextView = (TextView)pageOfView.findViewById(R.id.intro_detail);
					if(itemData.getIntroDetailResId() > 0)
					{
						detailTextView.setVisibility(View.VISIBLE);
						detailTextView.setText(itemData.getIntroDetailResId());
					}
					else
					{
						detailTextView.setVisibility(View.GONE);
					}
				}

				@Override
				public View initViewOfPage(Activity activity, PagerAdapter adapter, int viewType)
				{
					return LayoutInflater.from(SplashActivity.this).inflate(R.layout.intro_page_layout, null);
				}
			});
		}
		else
		{
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					startActivity(new Intent(SplashActivity.this, HomeActivity.class));
					finish();
				}
			}, 2000);
		}
	}
	
	/**
	 * 初始化介绍信息
	 * @return
	 */
	private ArrayList<IntroductionInfo> initIntroductionInfos()
	{
		ArrayList<IntroductionInfo> introductionInfos = new ArrayList<IntroductionInfo>();
		for (int i = 0; i < introTitleResIds.length; i++)
		{
			IntroductionInfo introductionInfo = new IntroductionInfo();
			introductionInfo.setIntroTitleResId(introTitleResIds[i]);
			introductionInfo.setIntroImgResId(introImgResIds[i]);
			introductionInfo.setIntroDetailResId(introDetailResIds[i]);
			
			introductionInfos.add(introductionInfo);
		}
		return introductionInfos;
	}
	
	@OnClick(R.id.get_start_button)
	public void onClick(View view)
	{
		startActivity(new Intent(this, HomeActivity.class));
		this.finish();
	}
}
