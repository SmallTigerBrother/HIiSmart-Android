package com.mn.tiger.upgrade;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lepow.hiremote.R;
import com.lepow.hiremote.widget.HSAlertDialog;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.download.TGDownloadManager;
import com.mn.tiger.download.TGDownloadParams;
import com.mn.tiger.download.TGDownloader;
import com.mn.tiger.download.observe.TGDownloadObserver;
import com.mn.tiger.media.TGAudioPlayer;
import com.mn.tiger.request.HttpType;
import com.mn.tiger.utility.PackageUtils;
import com.mn.tiger.utility.ToastUtils;

import java.io.File;

/**
 * Created by Dalang on 2015/8/1.
 */
public class AlarmAlertActivity extends Activity
{
    private static final int DOWNLOADING_NOTIFICATION_ID = 100;

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
