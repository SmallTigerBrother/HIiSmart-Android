package com.lepow.hiremote.widget;

import android.app.Dialog;
import android.content.Context;

import com.lepow.hiremote.R;

/**
 * Created by Dalang on 2015/9/11.
 */
public class ProgressDialog extends Dialog
{
    public ProgressDialog(Context context)
    {
        super(context, R.style.tiger_progressDialog);
        setContentView(R.layout.progress_dialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }
}
