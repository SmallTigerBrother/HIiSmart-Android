package com.mn.tiger.location;

/**
 * Google地址解析功能
 */
public class AMapGeoCoding
{
    /**
     * 执行地址解析
     * @param latitude
     * @param longitude
     * @param listener
     */
    public static void geoCoding(final double latitude, final double longitude,
                                 final GeoCodeListener listener)
    {

    }

    /**
     * 地址回调接口
     */
    public static interface GeoCodeListener
    {
        /**
         * 地址解析成功
         * @param result
         */
        void onGeoCodingSuccess(GeoCodeResult result);

        /**
         * 地址解析失败
         * @param code
         * @param message
         */
        void onGeoCodingError(int code, String message);
    }

}
