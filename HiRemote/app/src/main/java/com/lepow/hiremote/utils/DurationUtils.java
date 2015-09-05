package com.lepow.hiremote.utils;

import com.mn.tiger.utility.DateUtils;

/**
 * Created by Dalang on 2015/9/5.
 */
public class DurationUtils
{
    public static String duration2String(int duration)
    {
        if(duration < 1000)
        {
            duration = 1000;
        }

        return DateUtils.date2String(duration + 16 * 3600000, DateUtils.TIME_FORMAT);
    }
}
