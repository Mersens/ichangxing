package com.cxwl.ichangxing.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


public class LocationService extends Service {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation();
        return START_STICKY;
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setInterval(2000);//定位间隔
        option.setNeedAddress(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.startLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                  //可在其中解析amapLocation获取相应内容。
                    Log.e("LocationService",amapLocation.getLatitude()+":"+amapLocation.getLongitude());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }

        super.onDestroy();
    }
}
