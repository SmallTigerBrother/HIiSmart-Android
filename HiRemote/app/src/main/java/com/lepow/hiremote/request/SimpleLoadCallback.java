package com.lepow.hiremote.request;

import android.app.Activity;
import android.content.Context;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.request.TGHttpLoader.OnLoadCallback;
import com.mn.tiger.request.error.TGHttpError;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.ToastUtils;

/**
 * 简单的loadcallback接口
 *
 * @param <T>
 */
public abstract class SimpleLoadCallback<T> implements OnLoadCallback<T>
{
    private Context context;

    public SimpleLoadCallback(Context context)
    {
        this.context = context;
    }

    @Override
    public void onLoadStart()
    {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onLoadError(int code, String message, TGHttpResult httpResult)
    {
        // 捕捉移除
        Response<T> response = (Response<T>) httpResult.getObjectResult();
        if (null == response)
        {
            switch (code)
            {
                case TGHttpError.NO_NETWORK:
                    ToastUtils.showToast(context, TGHttpError.getDefaultErrorMsg(context, code));
                    break;
                case TGHttpError.SOCKET_TIMEOUT:
                    ToastUtils.showToast(context, TGHttpError.getDefaultErrorMsg(context, code));
                    break;
                case TGHttpError.IOEXCEPTION:
                    ToastUtils.showToast(context, TGHttpError.getDefaultErrorMsg(context, code));
                    break;
                case TGHttpError.UNKNOWN_EXCEPTION:
                    ToastUtils.showToast(context, TGHttpError.getDefaultErrorMsg(context, code));
                    break;

                default:
                    ToastUtils.showToast(context, "异常" + code);

                    break;
            }
        }
        else
        {
            switch (response.code)
            {
                case Response.RESULT_FAILED:
                    if (context instanceof Activity)
                    {
                        ToastUtils.showToast(context, response.message);
                    }

                    break;

                case Response.RESULT_EXPIRED:
                    if (context instanceof Activity)
                    {
                        ToastUtils.showToast(context, response.message);
                    }

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onLoadOver()
    {
        dismissLoadingBar();
    }

    @Override
    public void onLoadCache(T result, TGHttpResult httpResult)
    {
    }

    protected Context getContext()
    {
        return context;
    }

    /**
     * 显示进度条
     */
    protected void showLoadingBar()
    {
        if (context instanceof TGActionBarActivity)
        {
            ((TGActionBarActivity) context).showLoadingDialog();
        }
    }

    /**
     * 隐藏进度条
     */
    protected void dismissLoadingBar()
    {
        if (context instanceof TGActionBarActivity)
        {
            ((TGActionBarActivity) context).dismissLoadingDialog();
        }
    }
}
