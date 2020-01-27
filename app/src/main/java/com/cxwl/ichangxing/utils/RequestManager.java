package com.cxwl.ichangxing.utils;

import android.util.Log;

import com.cxwl.ichangxing.apis.ServiceStore;
import com.cxwl.ichangxing.app.App;
import com.cxwl.ichangxing.app.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by Mersens on 2016/9/12.
 */
public class RequestManager {
    public final static int CONNECT_TIMEOUT = 10;
    public final static int READ_TIMEOUT = 20;
    public final static int WRITE_TIMEOUT = 10;
    public Retrofit mRetrofit;
    private static RequestManager mRequestManager;//管理者实例
    public OkHttpClient mClient;//OkHttpClient实例
    public ServiceStore mServiceStore;//请求接口

    private RequestManager() {
        init();
    }

    //单例模式，对提供管理者实例
    public static RequestManager getInstance() {
        if (mRequestManager == null) {
            synchronized (RequestManager.class) {
                if (mRequestManager == null) {
                    mRequestManager = new RequestManager();
                }
            }
        }
        return mRequestManager;
    }

    private void init() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        mClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.WEBSERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();

        mServiceStore = mRetrofit.create(ServiceStore.class);
    }

    public interface onRequestCallBack {
        void onSuccess(String msg);
        void onError(String error);
    }

}
