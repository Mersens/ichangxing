package com.cxwl.ichangxing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Mersens
 * @title SharePreferenceUtil
 * @description:SharePreference工具类，数据存储
 * @time 2016年4月6日
 */
public class SPreferenceUtil {
    private static SPreferenceUtil sp;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "_sharedinfo";
    private static final String IS_FIRST = "is_first";
    private static final String USER_ID = "user_id";
    private static final String USERINFO = "userinfo";
    private static final String TOKEN = "token";
    public static Boolean getIsFirst() {
        return mSharedPreferences.getBoolean(IS_FIRST, true);
    }

    public static void setIsFirst(Boolean isIsFirst) {
        editor.putBoolean(IS_FIRST, isIsFirst);
        editor.commit();

    }

    private SPreferenceUtil() {

    }
    public static synchronized SPreferenceUtil getInstance(Context context) {
        if (sp == null) {
            sp = new SPreferenceUtil();
            mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            editor = mSharedPreferences.edit();
        }
        return sp;
    }




    public String getUserId() {
        return mSharedPreferences.getString(USER_ID,null);
    }
    public void setUserId(String userid) {
        editor.putString(USER_ID, userid);
        editor.commit();
    }
    public String getToken() {
        return mSharedPreferences.getString(TOKEN,null);
    }
    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getUserinfo() {
        return mSharedPreferences.getString(USERINFO, null);
    }

    public void setUserinfo(String info) {
        editor.putString(USERINFO, info);
        editor.commit();
    }
}
