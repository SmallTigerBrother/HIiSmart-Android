package com.mn.tiger.media;

/**
 * Created by peng on 15/8/2.
 */
public class TGRecorder
{
    private volatile static TGRecorder instance;

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

    }

    public void start()
    {

    }

    public void stop()
    {

    }
}
