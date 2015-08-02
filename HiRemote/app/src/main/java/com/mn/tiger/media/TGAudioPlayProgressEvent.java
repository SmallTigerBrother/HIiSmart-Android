package com.mn.tiger.media;

/**
 * Created by peng on 15/8/2.
 */
public class TGAudioPlayProgressEvent
{
    private int progress;

    public TGAudioPlayProgressEvent(int progress)
    {
        this.progress = progress;
    }

    public int getProgress()
    {
        return progress;
    }
}
