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
import com.mn.tiger.app.TGApplicationProxy;
import com.mn.tiger.download.TGDownloadManager;
import com.mn.tiger.download.TGDownloadParams;
import com.mn.tiger.download.TGDownloader;
import com.mn.tiger.download.observe.TGDownloadObserver;
import com.mn.tiger.request.HttpType;
import com.mn.tiger.utility.PackageUtils;
import com.mn.tiger.utility.ToastUtils;

import java.io.File;

/**
 * Created by Dalang on 2015/8/1.
 */
public class TGUpgradeDialogActivity extends Activity
{
    private static final int DOWNLOADING_NOTIFICATION_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVisible(false);

        int upgradeMode = TGUpgradeManager.getUpgradeModeFromIntent(getIntent());
        String latestVersion = TGUpgradeManager.getLatestVersionFromIntent(getIntent());
        String upgradeDescription = TGUpgradeManager.getDesciptionFromIntent(getIntent());
        String packageUrl = TGUpgradeManager.getPackageUrlFromIntent(getIntent());
        String upgradeOptions = TGUpgradeManager.getOptionsFromIntent(getIntent());

        switch (upgradeMode)
        {
            case IUpgradeDataParser.MODE_PROMPT:
                showPromptUpgradeDialog(latestVersion, upgradeDescription, packageUrl);
                break;

            case IUpgradeDataParser.MODE_FORCE:
                showForceUpgradeDialog(latestVersion, upgradeDescription, packageUrl);
                break;

            case IUpgradeDataParser.MODE_INVALID:
                showAppInvalidDialog();
                break;

            default:
                break;
        }
    }

    private void showPromptUpgradeDialog(final  String latestVersion, String description,
                                         final String packageUrl)
    {
        HSAlertDialog dialog = new HSAlertDialog(this);
        dialog.setTitleText(getString(R.string.tips));
        dialog.setBodyText(getString(R.string.prompt_upgrade_string_format, latestVersion, description));
        dialog.setLeftButton(getString(R.string.upgrade_later), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //关闭界面
                dialog.dismiss();
                TGUpgradeDialogActivity.this.finish();
            }
        });

        dialog.setRightButton(getString(R.string.upgrade_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, int which)
            {
                downloadPackage(latestVersion, packageUrl, new TGDownloadObserver()
                {
                    @Override
                    public void onProgress(TGDownloader downloader, int progress)
                    {
                        //将进度推送到通知栏
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(TGUpgradeDialogActivity.this);
                        builder.setOngoing(true);
                        builder.setProgress(100, progress, true);
                        builder.setSmallIcon(R.drawable.add_device);
                        builder.setContentTitle(getString(R.string.downloading));

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(DOWNLOADING_NOTIFICATION_ID, builder.build());
                    }

                    @Override
                    public void onSuccess(TGDownloader downloader)
                    {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(DOWNLOADING_NOTIFICATION_ID);

                        PackageUtils.installPackage(TGUpgradeDialogActivity.this, downloader.getSavePath());
                    }

                    @Override
                    public void onFailed(TGDownloader downloader, int errorCode, String errorMessage)
                    {
                        ToastUtils.showToast(TGUpgradeDialogActivity.this, R.string.something_wrong_downloading_try_later);
                    }
                });
                dialog.dismiss();
                TGUpgradeDialogActivity.this.finish();
            }
        });

        dialog.show();
    }

    private void showForceUpgradeDialog(final String latestVersion, String description,
                                        final String packageUrl)
    {
        final HSAlertDialog upgradeDialog = new HSAlertDialog(this);
        upgradeDialog.setTitleText(getString(R.string.tips));
        upgradeDialog.setBodyText(getString(R.string.force_upgrade_string_format, latestVersion, description));
        upgradeDialog.setLeftButton(getString(R.string.exit_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                TGApplicationProxy.getInstance().exit();
            }
        });

        upgradeDialog.setRightButton(getString(R.string.upgrade_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                upgradeDialog.setTitleText(getString(R.string.downloading));

                LinearLayout progressLayout = (LinearLayout) LayoutInflater.from(
                        TGUpgradeDialogActivity.this).inflate(R.layout.download_progress_layout, null);
                final ProgressBar progressBar = (ProgressBar) progressLayout.findViewById(R.id.download_package_progress);
                upgradeDialog.setBodyContentView(progressLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                downloadPackage(latestVersion, packageUrl, new TGDownloadObserver()
                {
                    @Override
                    public void onProgress(TGDownloader downloader, int progress)
                    {
                        progressBar.setProgress(progress);
                    }

                    @Override
                    public void onSuccess(TGDownloader downloader)
                    {
                        progressBar.setProgress(100);
                        PackageUtils.installPackage(TGUpgradeDialogActivity.this, downloader.getSavePath());
                    }

                    @Override
                    public void onFailed(TGDownloader downloader, int errorCode, String errorMessage)
                    {
                        ToastUtils.showToast(TGUpgradeDialogActivity.this, R.string.something_wrong_downloading_try_again);
                    }
                });
            }
        });

        upgradeDialog.setCancelable(false);
        upgradeDialog.setCanceledOnTouchOutside(false);
        upgradeDialog.show();
    }

    private void showAppInvalidDialog()
    {
        HSAlertDialog dialog = new HSAlertDialog(this);
        dialog.setTitleText(getString(R.string.tips));
        dialog.setBodyText(getString(R.string.your_app_is_invalid));
        dialog.setMiddleButton(getString(R.string.exit_now), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                TGApplicationProxy.getInstance().exit();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void downloadPackage(String latestVersion, String packageUrl, TGDownloadObserver observer)
    {
        TGDownloadManager downloadManager = new TGDownloadManager(this);
        TGDownloadParams downloadParams = new TGDownloadParams();
        downloadParams.setRequestType(HttpType.REQUEST_GET);
        downloadParams.setSavePath(TGApplicationProxy.getInstance().getApplication().getExternalCacheDir() +
                File.separator + latestVersion + ".apk");
        downloadParams.setUrl(packageUrl);
        int taskId = downloadManager.start(downloadParams);
        downloadManager.registerDownloadObserver(taskId,observer);
    }
}
