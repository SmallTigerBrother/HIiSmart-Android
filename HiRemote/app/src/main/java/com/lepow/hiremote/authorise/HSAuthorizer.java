package com.lepow.hiremote.authorise;

import android.app.Activity;

import com.lepow.hiremote.authorise.data.UserInfo;
import com.lepow.hiremote.misc.ServerUrls;
import com.lepow.hiremote.request.HttpLoader;
import com.lepow.hiremote.request.SimpleLoadCallback;
import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.ILogoutCallback;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.authorize.TGAuthorizer;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

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
    protected void executeAuthorize(final IAuthorizeCallback authorizeCallback)
    {
        HttpLoader<UserInfo> httpLoader = new HttpLoader<UserInfo>();
        httpLoader.addRequestParam("email", account);
        httpLoader.addRequestParam("password", password);
        httpLoader.loadByPost(getActivity(), ServerUrls.LOGIN, UserInfo.class, new SimpleLoadCallback<UserInfo>(getActivity())
        {
            @Override
            public void onLoadSuccess(UserInfo userInfo, TGHttpResult tgHttpResult)
            {
                TGAuthorizer.saveUserInfo(getActivity(), userInfo);
                TGAuthorizeResult authorizeResult = new TGAuthorizeResult();
                authorizeResult.setUID(userInfo.getId() + "");
                authorizeResult.setAccessToken(userInfo.getToken());
                authorizeCallback.onSuccess(authorizeResult);
            }

            @Override
            public void onLoadError(int code, String message, TGHttpResult httpResult)
            {
                super.onLoadError(code, message, httpResult);
                authorizeCallback.onError(code, message, message);
            }
        });
    }

    @Override
    protected void executeLogout(final ILogoutCallback logoutCallback)
    {
        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("id", ((UserInfo)TGAuthorizer.getUserInfo(getActivity())).getId() + "");
        httpLoader.loadByPost(getActivity(), ServerUrls.LOGOUT, Void.class, new SimpleLoadCallback<Void>(getActivity())
        {
            @Override
            public void onLoadSuccess(Void result, TGHttpResult tgHttpResult)
            {
                logoutCallback.onSuccess();
            }

            @Override
            public void onLoadError(int code, String message, TGHttpResult httpResult)
            {
                super.onLoadError(code, message, httpResult);
                logoutCallback.onError(code, message, message);
            }
        });
    }

    @Override
    public void register(String account, String password, final IRegisterCallback callback, Object... args)
    {
        String userName = (String)args[0];

        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("email", account);
        httpLoader.addRequestParam("password", password);
        httpLoader.addRequestParam("userName", userName);
        httpLoader.loadByPost(getActivity(), ServerUrls.REGISTER, Void.class, new SimpleLoadCallback<Void>(getActivity())
        {
            @Override
            public void onLoadSuccess(Void result, TGHttpResult tgHttpResult)
            {
                callback.onSuccess();
            }

            @Override
            public void onLoadError(int code, String message, TGHttpResult httpResult)
            {
                super.onLoadError(code, message, httpResult);
                callback.onError(code, message);
            }
        });
    }

    public void requestResetPassword(String email,  TGHttpLoader.OnLoadCallback<Void> callback)
    {
        HttpLoader<Void> httpLoader = new HttpLoader<Void>();
        httpLoader.addRequestParam("email", email);
        httpLoader.loadByPost(getActivity(), ServerUrls.RESET_PASSWORD, Void.class, callback);
    }
}

