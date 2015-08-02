package com.mn.tiger.media;

import android.media.MediaPlayer;
import android.os.Handler;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.log.Logger;

/**
 * Created by peng on 15/8/2.
 */
public class TGAudioPlayer
{
    private static final Logger LOG = Logger.getLogger(TGAudioPlayer.class);

    private volatile static TGAudioPlayer instance;

    private Handler timeHandler;

    private int progress = 0;

    private MediaPlayer mediaPlayer;

    public static TGAudioPlayer getInstance()
    {
        if (null == instance)
        {
            synchronized (TGAudioPlayer.class)
            {
                if (null == instance)
                {
                    instance = new TGAudioPlayer();
                }
            }
        }
        return instance;
    }

    private TGAudioPlayer()
    {
        mediaPlayer = new MediaPlayer();
        timeHandler = new Handler();
    }

    public void start(String dataSource)
    {
        start(dataSource, null);
    }

    public void start(String dataSource, MediaPlayer.OnCompletionListener listener)
    {
        progress = 0;
        try
        {
            mediaPlayer.reset(); //重置多媒体
            mediaPlayer.setDataSource(dataSource);//为多媒体对象设置播放路径
            mediaPlayer.prepare();//准备播放
            mediaPlayer.start();//开始播放
            //setOnCompletionListener 当前多媒体对象播放完成时发生的事件
            if(null != listener)
            {
                mediaPlayer.setOnCompletionListener(null);
            }

            final int updateProgressTimeLength = mediaPlayer.getDuration() / 100;

            timeHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    progress++;
                    TGApplication.getBus().post(new TGAudioPlayProgressEvent(progress));
                    if(progress < 100)
                    {
                        timeHandler.postDelayed(this,updateProgressTimeLength);
                    }
                }
            }, updateProgressTimeLength);
        }
        catch (Exception e)
        {
            LOG.e(e);
        }
    }

    public void toggle()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.start();
        }
    }

    public void stop()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();

            progress = 0;
            TGApplication.getBus().post(new TGAudioPlayProgressEvent(progress));
        }
    }
}