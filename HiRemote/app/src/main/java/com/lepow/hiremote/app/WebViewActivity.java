package com.lepow.hiremote.app;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lepow.hiremote.R;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.utility.ToastUtils;
import com.mn.tiger.widget.TGNavigationBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity
{
	@Bind(R.id.webview)
	WebView mWebView;

	@Bind(R.id.progress)
	View mProgress;

	private String barTitle;

	private String url;

	/**
	 * 额外添加的header
	 */
	private Map<String, String> addtionalHeader = new HashMap<String, String>();

	/**
	 * 消息头里面的Referer字段
	 */
	private String refererUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		ButterKnife.bind(this);

		barTitle = getIntent().getStringExtra(IntentKeys.WEBVIEW_ACTIVITY_TITLE);
		url = getIntent().getStringExtra(IntentKeys.URL);
		setupViews();
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
	}

	private void setupViews()
	{
		setBarTitleText(barTitle);
		showLeftBarButton(true);

		mWebView.getSettings().setJavaScriptEnabled(true);
		// 创建WebViewClient对象
		WebViewClient wvc = new WebViewClient()
		{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器加载页面
				mWebView.loadUrl(url, getAddtionalHeader());
				refererUrl = url;
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
			}

			@Override
			public void onLoadResource(WebView view, String url)
			{
				super.onLoadResource(view, url);
			}

		};
		mWebView.setWebViewClient(wvc);
		mWebView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				DisplayMetrics metric = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metric);
				int width = metric.widthPixels;

				int progressUnit = width / 100;

				LayoutParams params = mProgress.getLayoutParams();
				params.width = progressUnit * newProgress;
				mProgress.setLayoutParams(params);

				if (newProgress == 100)
				{
					mProgress.setVisibility(View.GONE);
				}
				else
				{
					mProgress.setVisibility(View.VISIBLE);
				}

				super.onProgressChanged(view, newProgress);
			}
		});

		mWebView.addJavascriptInterface(new Object()
		{
			@JavascriptInterface
			public void back()
			{
				WebViewActivity.this.finish();
			}
		}, "pocketbuy");

		refererUrl = url;
		mWebView.loadUrl(url);
	}

	/**
	 * 后去additionalHeader
	 * 
	 * @return
	 */
	private Map<String, String> getAddtionalHeader()
	{
		addtionalHeader.put("Referer", refererUrl);
		return addtionalHeader;
	}

	public void showToast(int textResId)
	{
		ToastUtils.showToast(this, textResId);
	}
	
	public void showToast(String text)
	{
		ToastUtils.showToast(this, text);
	}

}
