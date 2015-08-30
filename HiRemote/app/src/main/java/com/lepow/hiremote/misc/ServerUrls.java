package com.lepow.hiremote.misc;

/**
 * Created by peng on 15/8/1.
 */
public class ServerUrls
{
    private static final boolean DEBUG = false;

    private static final String SERVER_URL = DEBUG ? "http://192.168.31.210:8090/lepow/api" : "http://app.lepow.net:8080/lepow/api";

    public static final String CHECK_UPGRADE_URL = SERVER_URL + "/apps";

    public static final String PRIVACY_POLICY = "http://hismart.us/help/privacy";

    public static final String TERMS_CONDITIONS = "http://hismart.us/help/privacy";

    public static final String SUPPORT_FAQ = "http://hismart.us/faq";

    public static final String CONTACT_US = "http://hismart.us/contact";

    public static final String LOGIN = SERVER_URL + "/member/login";

    public static final String REGISTER = SERVER_URL + "/member/register";

    public static final String FACEBOOK_LOGIN = SERVER_URL + "/member/facebook/login";

    public static final String LOGOUT = SERVER_URL + "/member/logout";

    public static final String RESET_PASSWORD = SERVER_URL + "/member/findpwd";

}
