package com.mn.tiger.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.czt.mp3recorder.MP3Recorder;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.log.Logger;
import com.mn.tiger.utility.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by peng on 15/8/2.
 */
public class TGRecorder
{
    private static final Logger LOG = Logger.getLogger(TGRecorder.class);

    private volatile static TGRecorder instance;

    private AudioManager audioManager;

    private MP3Recorder mp3Recorder;

    public static TGRecorder getInstance()
    {
        if(null == instance)
        {
            synchronized (TGRecorder.class)
            {
                if(null == instance)
                {
                    instance = new TGRecorder();
                }
            }
        }
        return instance;
    }

    private TGRecorder()
    {
        audioManager = (AudioManager)TGApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
    }

    public void start(String outputFilePath)
    {
        mp3Recorder = new MP3Recorder(FileUtils.createFile(outputFilePath));
        try
        {

//            audioManager.startBluetoothSco();
//            TGApplication.getInstance().registerReceiver(new BroadcastReceiver()
//            {
//                @Override
//                public void onReceive(Context context, Intent intent)
//                {
//
//                }
//            }, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
//
            mp3Recorder.start();
        }
        catch (IOException e)
        {
            LOG.e(e);
        }
    }

    public void stop()
    {
        mp3Recorder.stop();
    }
}
