package com.lepow.hiremote.Dialog;

import android.content.Context;
import android.graphics.Color;

import com.mn.tiger.widget.dialog.TGDialog;

/**
 * Created by Dalang on 2015/7/26.
 */
public class HRDialog extends TGDialog
{
    public HRDialog(Context context)
    {
        super(context,  new TGDialogParams(context)
        {
            @Override
            public int getLeftButtonTextColor()
            {
                return Color.BLACK;
            }

            @Override
            public int getRightButtonTextColor()
            {
                return Color.BLUE;
            }
        });

        getTitleLayout().setBackgroundColor(Color.GREEN);
    }

}
