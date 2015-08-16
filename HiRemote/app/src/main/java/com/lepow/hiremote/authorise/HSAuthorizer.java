package com.lepow.hiremote.authorise;

import android.app.Activity;

import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.ILogoutCallback;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.authorize.TGAuthorizer;

/**
 * Created by peng on 15/8/16.
 */
public class HSAuthorizer extends TGAuthorizer
{
    public HSAuthorizer(Activity activity, String account, String password)
    {
        super(activity, account, password);
    }

    @Override
    protected void executeAuthorize(IAuthorizeCallback authorizeCallback)
    {

    }

    @Override
    protected void executeLogout(ILogoutCallback logoutCallback)
    {

    }

    @Override
    public void register(String account, String password, IRegisterCallback callback, Object... args)
    {
        String email = (String)args[0];

    }

    public void requestResetPassword(String email)
    {

    }
}

