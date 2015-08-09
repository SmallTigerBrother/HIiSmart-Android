package com.lepow.hiremote.request;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.mn.tiger.log.Logger;
import com.mn.tiger.request.HttpType;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.async.TGHttpAsyncTask;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.Commons;
import com.mn.tiger.utility.PackageUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求类
 * @param <T>
 */
public class HttpLoader<T> extends TGHttpLoader<T>
{
    private static final Logger LOG = Logger.getLogger(HttpLoader.class);

    public HttpLoader()
    {
    }

    @Override
    protected void execute(Context context, int requestType, String requestUrl,
                           String resultClsName, com.mn.tiger.request.TGHttpLoader.OnLoadCallback<T> callback)
    {
        //加入默认参数
        this.addRequestParam("system", "android");
        this.addRequestParam("appVersion", PackageUtils.getPackageInfoByName(
                context, context.getPackageName()).versionName);
        this.addRequestParam("versionCode", PackageUtils.getPackageInfoByName(context,
                context.getPackageName()).versionCode + "");
        this.addRequestParam("systemVersion", Build.VERSION.RELEASE);
        this.addProperty("Accept-Language", Commons.getSystemLanguage(context));

        super.execute(context, requestType, requestUrl, resultClsName, callback);
    }

    @Override
    protected TGHttpAsyncTask<T> initAsyncTask()
    {
        return new InternalAsyncTask();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parseRequestResult(TGHttpResult httpResult, String resultClsName)
    {
        //解析数据
        try
        {
            Response<T> response = new Response<T>();
            JSONObject jsonObject = new JSONObject(httpResult.getResult());
            if(jsonObject.has("code"))
            {
                response.code = jsonObject.getInt("code");
            }

            if(jsonObject.has("message"))
            {
                response.message = jsonObject.getString("message");
            }

            if(jsonObject.has("data"))
            {
                response.rawData = jsonObject.get("data").toString();
                Gson gson = new Gson();
                try
                {
                    response.data = (T) gson.fromJson(jsonObject.get("data").toString(),
                            Class.forName(resultClsName));
                }
                catch (Exception e)
                {
                    LOG.e("[Method:parseRequestResult] url : "  + getAsyncTask().getRequestUrl() + "\n params : " +
                            getAsyncTask().getStringParams() + "\n" + e.getMessage());
                }
            }

            return response;
        }
        catch (JSONException e)
        {
            LOG.e("[Method:parseRequestResult] url : "  + getAsyncTask().getRequestUrl() + "\n params : " +
                    getAsyncTask().getStringParams() + "\n" + e.getMessage());
        }

        return null;
    }

    /**
     * 内部使用的异步任务类
     */
    private class InternalAsyncTask extends TGHttpAsyncTask<T>
    {
        public InternalAsyncTask()
        {
            super("", HttpType.REQUEST_GET, null);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected boolean hasError(TGHttpResult httpResult)
        {
            //检测自定义异常
            Response<T> response = (Response<T>) httpResult.getObjectResult();
            if(null != response)
            {
                return response.code != Response.RESULT_SUCCESS;
            }

            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected T parseResult(Object originalResultObject)
        {
            //返回T
            if(null != originalResultObject)
            {
                return ((Response<T>)originalResultObject).data;
            }

            return null;
        }
    }
}
