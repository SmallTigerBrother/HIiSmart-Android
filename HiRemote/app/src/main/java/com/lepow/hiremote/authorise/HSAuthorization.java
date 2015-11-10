package com.lepow.hiremote.authorise;

import android.app.Activity;

import com.lepow.hiremote.authorise.data.UserInfo;
import com.lepow.hiremote.misc.ServerUrls;
import com.lepow.hiremote.request.HttpLoader;
import com.lepow.hiremote.request.SimpleLoadCallback;
import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.ILogoutCallback;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.authorize.TGAuthorization;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * Created by peng on 15/8/16.
 */
public class HSAuthorization extends TGAuthorization
{
    private static HSAuthorization instance;

    public static HSAuthorization getInstance()
    {
        if(null == instance)
        {
            synchronized (HSAuthorization.class)
            {
                if(null == instance)
                {
                    instance = new HSAuthorization();
                }
            }
        }
        return instance;
    }

    private HSAuthorization()
    {
        super();
    }

    @Override
    protected void executeAuthorize(final Activity activity, final IAuthorizeCallback authorizeCallback)
    {
        HttpLoader<UserInfo> httpLoader = new HttpLoader<UserInfo>();
        httpLoader.addRequestParam("email", account);
        httpLoader.addRequestParam("password", password);
        httpLoader.loadByPost(activity, ServerUrls.LOGIN, UserInfo.class, new SimpleLoadCallback<UserInfo>(activity)
        {
            @Override
            public void onLoadSuccess(UserInfo userInfo, TGHttpResult tgHttpResult)
            {
                TGAuthorization.saveUserInfo(activity, userInfo);
                TGAuthorizeResult authorizeResult = new TGAuthorizeResult();
                authorizeResult.setUID(userInfo.getId() + "");
                authorizeResult.setAccessToken(userInfo.getToken());
                authorizeCallback.onAuthorizeSuccess(authorizeResult);
            }

            @Override
            public void onLoadError(int code, String message, TGHttpResult httpResult)
            {
                super.onLoadError(code, message, httpResult);
                authorizeCallback.onAuthorizeError(code, message, message);
            }
        });
    }

    @Override
    protected void executeLogout(Activity activity, final ILogoutCallback logoutCallback)
    {
        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("id", ((UserInfo)TGAuthorization.getUserInfo(activity)).getId() + "");
        httpLoader.loadByPost(activity, ServerUrls.LOGOUT, Void.class, new SimpleLoadCallback<Void>(activity)
        {
            @Override
            public void onLoadSuccess(Void result, TGHttpResult tgHttpResult)
            {
                logoutCallback.onLogoutSuccess();
            }

            @Override
            public void onLoadError(int code, String message, TGHttpResult httpResult)
            {
                super.onLoadError(code, message, httpResult);
                logoutCallback.onLogoutError(code, message, message);
            }
        });
    }

    @Override
    public void register(Activity activity, String account, String password, final IRegisterCallback callback, Object... args)
    {
        String userName = (String)args[0];

        HttpLoader<TGAuthorizeResult> httpLoader = new HttpLoader<TGAuthorizeResult>();
        httpLoader.addRequestParam("email", account);
        httpLoader.addRequestParam("password", password);
        httpLoader.addRequestParam("userName", userName);
        httpLoader.loadByPost(activity, ServerUrls.REGISTER, TGAuthorizeResult.class, new SimpleLoadCallback<TGAuthorizeResult>(activity)
        {
            @Override
            public void onLoadSuccess(TGAuthorizeResult result, TGHttpResult tgHttpResult)
            {
                callback.onRegisterSuccess(result);
            }

            @Override
            public void onLoadError(int code, String message, TGHttpResult httpResult)
            {
                super.onLoadError(code, message, httpResult);
                callback.onRegisterError(code, message);
            }
        });
    }

    public void requestResetPassword(Activity activity, String email,  TGHttpLoader.OnLoadCallback<Void> callback)
    {
        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("email", email);
        httpLoader.loadByPost(activity, ServerUrls.RESET_PASSWORD, Void.class, callback);
    }
}

