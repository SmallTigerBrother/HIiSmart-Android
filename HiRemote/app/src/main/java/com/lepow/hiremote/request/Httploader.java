package com.lepow.hiremote.request;

import android.content.Context;

import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * Created by Dalang on 2015/8/1.
 */
public class Httploader<T> extends TGHttpLoader<T>
{
    public static abstract class  SimpleOnLoadCallback<T> implements OnLoadCallback<T>
    {
        public SimpleOnLoadCallback(Context context)
        {

        }

        @Override
        public void onLoadStart()
        {

        }

        @Override
        public void onLoadError(int i, String s, TGHttpResult tgHttpResult)
        {

        }

        @Override
        public void onLoadCache(T t, TGHttpResult tgHttpResult)
        {

        }

        @Override
        public void onLoadOver()
        {

        }
    }
}
