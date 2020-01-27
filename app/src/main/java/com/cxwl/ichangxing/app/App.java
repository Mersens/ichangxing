package com.cxwl.ichangxing.app;

import android.app.Activity;

import android.app.Application;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.multidex.MultiDex;
import android.util.Log;


import com.cxwl.ichangxing.service.PlayerMusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class App extends Application {

    private static App sApp;
    private List<Activity> mList;

    public App(){
        mList=new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp=this;
        addLifecycleCallbacks();
        initKeepLive(this);
    }

    private void initKeepLive(Context context) {
        Intent intent= new Intent(context, PlayerMusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public static App getInstance(){
        if(sApp==null){
            synchronized (App.class){
                if(sApp==null){
                    sApp=new App();
                }
            }
        }
        return sApp;
    }


    private void addLifecycleCallbacks(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.e("onActivityCreated","onActivityCreated==="+activity.getClass().getName());
                mList.add(activity);
            }
            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mList.remove(activity);
                Log.e("onActivityDestroyed","onActivityDestroyed==="+activity.getClass().getName());
            }
        });

    }
    public void exit() {
        try {
            for (int i=0;i<mList.size();i++) {
                Activity activity=mList.get(i);
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }



}
