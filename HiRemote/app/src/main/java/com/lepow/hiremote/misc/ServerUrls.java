package com.lepow.hiremote.misc;

/**
 * Created by peng on 15/8/1.
 */
public class ServerUrls
{
    private static final boolean DEBUG = false;

    private static final String SERVER_URL = DEBUG ? "http://192.168.31.210:8090/lepow/api" : "http://app.lepow.net:8080/lepow/api";

    public static final String CHECK_UPGRADE_URL = SERVER_URL + "/apps";

    public static final String PRIVACY_POLICY = "";

    public static final String TERMS_CONDITIONS = "";

    public static final String SUPPORT_FAQ = "";

    public static final String CONTACT_US = "";
}
