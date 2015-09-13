package com.lepow.hiremote.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

import com.lepow.hiremote.R;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.media.TGAudioPlayer;

/**
 * Created by Dalang on 2015/8/1.
 */
public class AlarmAlertActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVisible(false);
        showAlertDialog();
    }

    private void showAlertDialog()
    {
        HSAlertDialog dialog = new HSAlertDialog(this);
        dialog.setTitleText(getString(R.string.tips));
        dialog.setBodyText(getString(R.string.alarm_tips));
        dialog.setMiddleButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                TGAudioPlayer.getInstance().stop();
                dialog.dismiss();
                AlarmAlertActivity.this.finish();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}
