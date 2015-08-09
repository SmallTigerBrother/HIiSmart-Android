package com.mn.tiger.media;

import android.media.MediaPlayer;
import android.os.Handler;

import com.mn.tiger.log.Logger;

/**
 * Created by peng on 15/8/2.
 */
public class TGAudioPlayer
{
    private static final Logger LOG = Logger.getLogger(TGAudioPlayer.class);

    private volatile static TGAudioPlayer instance;

    private static Handler timeHandler;

    private int progress = 0;

    private MediaPlayer mediaPlayer;

    private OnPlayListener onPlayListener;

    private String currentDataSource = "";

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

    public void start(final String dataSource, final OnPlayListener listener)
    {
        this.currentDataSource = dataSource;
        this.onPlayListener = listener;
        progress = 0;
        try
        {
            mediaPlayer.reset(); //重置多媒体
            mediaPlayer.setDataSource(currentDataSource);//为多媒体对象设置播放路径
            mediaPlayer.prepare();//准备播放
            mediaPlayer.start();//开始播放

            if(null != onPlayListener)
            {
                onPlayListener.onPlayStart(currentDataSource);
            }

            //setOnCompletionListener 当前多媒体对象播放完成时发生的事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    if(null != onPlayListener)
                    {
                        onPlayListener.onPlayComplete(currentDataSource);
                    }
                }
            });

            final int updateProgressTimeLength = mediaPlayer.getDuration() / 100;

            timeHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    progress++;
                    if (null != onPlayListener)
                    {
                        onPlayListener.onPlaying(currentDataSource, progress);
                    }

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
            if(null != onPlayListener)
            {
                onPlayListener.onPlayPause(currentDataSource);
            }
        }
        else
        {
            mediaPlayer.start();
            if(null != onPlayListener)
            {
                onPlayListener.onPlayStart(currentDataSource);
            }
        }
    }

    public void stop()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            progress = 0;
            if(null != onPlayListener)
            {
                onPlayListener.onPlayStop(currentDataSource);
            }
        }
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public static interface OnPlayListener
    {
        void onPlayStart(String dataSource);

        void onPlaying(String dataSource, int progress);

        void onPlayPause(String dataSource);

        void onPlayStop(String dataSource);

        void onPlayComplete(String dataSource);
    }
}
