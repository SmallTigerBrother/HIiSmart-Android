package com.mn.tiger.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRecorder;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.log.Logger;

import java.io.IOException;

/**
 * Created by peng on 15/8/2.
 */
public class TGRecorder
{
    private static final Logger LOG = Logger.getLogger(TGRecorder.class);

    private volatile static TGRecorder instance;

    private static int recordOutputFormat = MediaRecorder.OutputFormat.AAC_ADTS;

    private static int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;

    private AudioManager audioManager;

    private MediaRecorder mediaRecorder;

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
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setOutputFormat(recordOutputFormat);
        mediaRecorder.setAudioEncoder(audioEncoder);
        audioManager = (AudioManager)TGApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
    }

    public void start(int sourceInput, String outputFilePath)
    {
        mediaRecorder.setAudioSource(sourceInput);
        mediaRecorder.setOutputFile(outputFilePath);

        try
        {
            mediaRecorder.prepare();

            audioManager.startBluetoothSco();
            TGApplication.getInstance().registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {

                }
            }, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));

            mediaRecorder.start();
        }
        catch (IOException e)
        {
            LOG.e(e);
        }
    }

    public void stop()
    {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
    }

    public static int getAudioEncoder()
    {
        return audioEncoder;
    }

    public static void setAudioEncoder(int audioEncoder)
    {
        TGRecorder.audioEncoder = audioEncoder;
    }

    public static int getRecordOutputFormat()
    {
        return recordOutputFormat;
    }

    public static void setRecordOutputFormat(int recordOutputFormat)
    {
        TGRecorder.recordOutputFormat = recordOutputFormat;
    }
}
