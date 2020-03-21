package com.cxwl.ichangxing.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.adapter.MenuPageAdapter;
import com.cxwl.ichangxing.app.App;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.fragment.FindResourceFragment;
import com.cxwl.ichangxing.fragment.MyInfoFragment;
import com.cxwl.ichangxing.fragment.StartFragment;
import com.cxwl.ichangxing.fragment.WaybillFragment;
import com.cxwl.ichangxing.service.LocationService;
import com.cxwl.ichangxing.service.PlayerMusicService;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.NoScrollViewPager;
import com.google.gson.Gson;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.listener.OnResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final int SDK_PERMISSION_REQUEST = 127;
    private CompositeDisposable mCompositeDisposable;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>(Arrays.asList("接单",
            "找货",
            "发车",
            "我的"));
    private NoScrollViewPager mViewPager;
    private RelativeLayout mLayoutYD;
    private RelativeLayout mLayoutZH;
    private RelativeLayout mLayoutFC;
    private RelativeLayout mLayoutWD;

    private ImageView mImgYD;
    private ImageView mImgZH;
    private ImageView mImgFC;
    private ImageView mImgWD;

    private TextView mTextYD;
    private TextView mTextZH;
    private TextView mTextFC;
    private TextView mTextWD;
    private int index;
    private int selectColor;
    private int unSelectColor;
    private MenuPageAdapter pageAdapter;
    private TextView mTextTitle;
    private RelativeLayout mLayoutToolbar;
    private TextView mTextRightTitle;
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void init() {
        selectColor = getResources().getColor(R.color.actionbar_color);
        unSelectColor = getResources().getColor(R.color.color_text_gray);
        initViews();
        getPersimmions();
        initEvents();
        initdatas();
        initOpenApi(this);
        initBus();
    }

    private void initOpenApi(MainActivity mainActivity) {
        LocationOpenApi.init(mainActivity,
                Constants.LOCATION_API_APPID,
                Constants.LOCATION_APPSECURITY,
                Constants.LOCATION_API_ENTERPRISESENDERCODE,
                Constants.LOCATION_API_ENVIRONMENT,
                new OnResultListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("initOpenApi", "initOpenApi onSuccess");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.e("initOpenApi", "initOpenApi onFailure=" + s + ";" + s1);
                    }
                });

    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void initBus() {
        mCompositeDisposable = new CompositeDisposable();
        //监听订阅事件
        Disposable d = RxBus.getInstance()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (o instanceof EventEntity) {
                            EventEntity e = (EventEntity) o;
                            int type = e.type;
                            if (type == EventEntity.START_CAR) {
                                RxBus.getInstance().send(new EventEntity(EventEntity.START_CAR_REFRESH));
                                index = 2;
                                setTabColor(index);
                            } else if (type == EventEntity.FINISH) {
                                MainActivity.this.finish();
                                SPreferenceUtil.getInstance(MainActivity.this).setUserId(null);
                                SPreferenceUtil.getInstance(MainActivity.this).setUserinfo(null);
                                SPreferenceUtil.getInstance(MainActivity.this).setToken(null);
                            }
                        }
                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        mCompositeDisposable.add(d);

    }

    private void initEvents() {
        mLayoutYD.setOnClickListener(this);
        mLayoutZH.setOnClickListener(this);
        mLayoutFC.setOnClickListener(this);
        mLayoutWD.setOnClickListener(this);
        mTextRightTitle.setOnClickListener(this);
        fragments.add(WaybillFragment.newInstance());
        fragments.add(FindResourceFragment.newInstance());
        fragments.add(StartFragment.newInstance());
        fragments.add(MyInfoFragment.newInstance());
        mViewPager.setOffscreenPageLimit(mTitles.size());
        pageAdapter = new MenuPageAdapter(getSupportFragmentManager()
                , fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initdatas() {

        String token = SPreferenceUtil.getInstance(MainActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(MainActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        getUserInfo(token);
        String userinfo = SPreferenceUtil.getInstance(this).getUserinfo();
       /* if(!TextUtils.isEmpty(userinfo) ){
            Gson gson=new Gson();
            User user=gson.fromJson(userinfo,User.class);
            setUseInfo(user);
        }else {
            Toast.makeText(this, "用户信息为空，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }*/
    }

    private void getUserInfo(String token) {
        RequestManager.getInstance()
                .mServiceStore
                .getUserInfo(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("incomeSt", "incomeSt==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    SPreferenceUtil.getInstance(MainActivity.this)
                                            .setUserinfo(jsonObject.toString());
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(jsonObject.toString(), User.class);
                                    setUseInfo(user);
                                } else if (object.getInt("code") == 2) {
                                    String message = object.getString("message");
                                    showTokenView(message);
                                } else {
                                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(MainActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void showTokenView(String message) {
        final ExitDialogFragment exitDialogFragment = ExitDialogFragment.getInstance(message);
        exitDialogFragment.showF(getSupportFragmentManager(), "showTokenView");
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
                SPreferenceUtil.getInstance(MainActivity.this).setUserId(null);
                SPreferenceUtil.getInstance(MainActivity.this).setUserinfo(null);
                SPreferenceUtil.getInstance(MainActivity.this).setToken(null);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("mobile", "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });

    }

    private void initViews() {
        mLayoutYD = findViewById(R.id.layout_yd);
        mLayoutZH = findViewById(R.id.layout_zh);
        mLayoutFC = findViewById(R.id.layout_fc);
        mLayoutWD = findViewById(R.id.layout_wd);

        mImgYD = findViewById(R.id.img_yd);
        mImgZH = findViewById(R.id.img_zh);
        mImgFC = findViewById(R.id.img_fc);
        mImgWD = findViewById(R.id.img_wd);

        mTextYD = findViewById(R.id.tv_yd);
        mTextZH = findViewById(R.id.tv_zh);
        mTextFC = findViewById(R.id.tv_fc);
        mTextWD = findViewById(R.id.tv_wd);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setScroll(false);
        mLayoutToolbar = findViewById(R.id.layout_toolbar);

        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("运单");
        mTextRightTitle = findViewById(R.id.tv_right_title);
        mTextRightTitle.setVisibility(View.GONE);
    }

    private void setUseInfo(User user) {
        if (user != null) {
            status = user.getVerificationStatus();
            if (user.getVerificationStatus() == 0) {
                //未认证
                showRzRusultView("用户未认证,请进行实名认证！");

            } else if (user.getVerificationStatus() == 3) {
                //认证失败
                showRzRusultView("用户认证未通过,请重新进行实名认证！");
            }

        }
    }

    private void showRzRusultView(String msg) {
        final ExitDialogFragment dialogFragment = ExitDialogFragment.getInstance(msg);
        dialogFragment.showF(getSupportFragmentManager(), "showRzRusultView");
        dialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                dialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                dialogFragment.dismissAllowingStateLoss();
                Intent intent = new Intent(MainActivity.this, UserRzActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_yd:
                index = 0;
                setTabColor(index);
                break;
            case R.id.layout_zh:
                index = 1;
                setTabColor(index);
                break;
            case R.id.layout_fc:
                index = 2;
                setTabColor(index);
                break;
            case R.id.layout_wd:
                index = 3;
                setTabColor(index);
                break;
            case R.id.tv_right_title:
                Intent mIntent = new Intent(MainActivity.this, StartCarRecordActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
        mViewPager.setCurrentItem(index);
    }

    private void setTabColor(int index) {
        resetTab();
        switch (index) {
            case 0:
                mTextTitle.setText(mTitles.get(0));
                mLayoutToolbar.setVisibility(View.VISIBLE);
                mTextYD.setTextColor(selectColor);
                mTextRightTitle.setVisibility(View.GONE);
                mImgYD.setImageResource(R.mipmap.icon_yd_select);
                break;
            case 1:
                mTextTitle.setText(mTitles.get(1));
                mLayoutToolbar.setVisibility(View.VISIBLE);
                mTextZH.setTextColor(selectColor);
                mTextRightTitle.setVisibility(View.GONE);
                mImgZH.setImageResource(R.mipmap.icon_hy_select);
                break;
            case 2:
                mTextTitle.setText(mTitles.get(2));
                mLayoutToolbar.setVisibility(View.VISIBLE);
                mTextFC.setTextColor(selectColor);
                mTextRightTitle.setVisibility(View.VISIBLE);
                mTextRightTitle.setText("发车记录");
                mImgFC.setImageResource(R.mipmap.icon_fc_select);
                break;
            case 3:
                mTextTitle.setText(mTitles.get(3));
                mLayoutToolbar.setVisibility(View.VISIBLE);
                mTextWD.setTextColor(selectColor);
                mTextRightTitle.setVisibility(View.GONE);
                mImgWD.setImageResource(R.mipmap.icon_wd_select);
                break;
        }
    }

    private void resetTab() {
        mTextYD.setTextColor(unSelectColor);
        mImgYD.setImageResource(R.mipmap.icon_yd_normal);
        mTextZH.setTextColor(unSelectColor);
        mImgZH.setImageResource(R.mipmap.icon_hy_normal);
        mTextFC.setTextColor(unSelectColor);
        mImgFC.setImageResource(R.mipmap.icon_fc_normal);
        mTextWD.setTextColor(unSelectColor);
        mImgWD.setImageResource(R.mipmap.icon_wd_normal);
    }

    @Override
    public void onBackPressed() {
        confirmExit("确定退出应用?");
    }

    private void confirmExit(String msg) {
        //退出操作
        final ExitDialogFragment dialog = ExitDialogFragment.getInstance(msg);
        dialog.showF(getSupportFragmentManager(), "finishDialog");

        dialog.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                dialog.dismissAllowingStateLoss();
                Intent intent = new Intent(getApplicationContext(), PlayerMusicService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
                App.getInstance().exit();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();

    }
}
