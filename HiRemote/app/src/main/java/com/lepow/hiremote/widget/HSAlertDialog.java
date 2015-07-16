package com.lepow.hiremote.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mn.tiger.widget.dialog.TGDialog;
import com.lepow.hiremote.R;

/**
 * 带底部按钮的对话框基类
 */
public class HSAlertDialog extends TGDialog
{
	public HSAlertDialog(Context context)
	{
		super(context, new DialogParams(context));
	}
	
	/**
	 * 自定义对话框参数，包括背景色、字体大小、颜色、按钮背景等
	 */
	private static class DialogParams extends TGDialogParams
	{
		public DialogParams(Context context)
		{
			super(context);
		}

		@Override
		public Drawable getBackgroundResource()
		{
			return getContext().getResources().getDrawable(R.drawable.alert_dialog_bg);
		}
		
		@Override
		public float getBodyTextSize()
		{
			return 20f;
		}
		
		@Override
		public int getBodyTextColor()
		{
			return getContext().getResources().getColor(R.color.text_color_normal);
		}
		
		@Override
		public float getTitleTextSize()
		{
			return 20f;
		}
		
		@Override
		public int getTitleTextColor()
		{
			return getContext().getResources().getColor(R.color.text_color_normal);
		}
		
		@Override
		public int getLeftButtonTextColor()
		{
			return getContext().getResources().getColor(R.color.dialog_btn_blue);
		}
		
		@Override
		public Drawable getLeftButtonBackgroundRes()
		{
			return getContext().getResources().getDrawable(R.drawable.dialog_left_btn_bg);
		}
		
		@Override
		public float getLeftButtonTextSize()
		{
			return 20f;
		}
		
		@Override
		public float getRightButtonTextSize()
		{
			return 20f;
		}
		
		@Override
		public int getRightButtonTextColor()
		{
			return getContext().getResources().getColor(R.color.dialog_btn_blue);
		}
		
		@Override
		public Drawable getRightButtonBackgroundRes()
		{
			return getContext().getResources().getDrawable(R.drawable.dialog_right_btn_bg);
		}
		
		@Override
		public int getMiddleButtonTextColor()
		{
			return getContext().getResources().getColor(R.color.dialog_btn_blue);
		}
		
		@Override
		public Drawable getMiddleButtonBackgroundRes()
		{
			return getContext().getResources().getDrawable(R.drawable.dialog_middle_btn_bg);
		}
		
		@Override
		public float getMiddleButtonTextSize()
		{
			return 20f;
		}
		
		@Override
		public int getButtonWidthOfOne()
		{
			return 1 * getDialogWidth();
		}
		
		@Override
		public int getButtonWidthOfDouble()
		{
			return (int) (0.5 * getDialogWidth());
		}
		
		@Override
		public int getButtonWidthOfThree()
		{
			return (int) (0.333 * getDialogWidth());
		}
	}
	
}
