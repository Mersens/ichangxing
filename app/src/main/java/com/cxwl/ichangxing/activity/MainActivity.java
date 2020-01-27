package com.cxwl.ichangxing.activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private int status=0;
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
                            if (type==EventEntity.START_CAR) {
                                RxBus.getInstance().send(new EventEntity(EventEntity.START_CAR_REFRESH));
                                mViewPager.setCurrentItem(2);
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

        String token= SPreferenceUtil.getInstance(MainActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(MainActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        getUserInfo(token);
        String userinfo= SPreferenceUtil.getInstance(this).getUserinfo();
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
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    SPreferenceUtil.getInstance(MainActivity.this)
                                            .setUserinfo(jsonObject.toString());
                                    Gson gson=new Gson();
                                    User user=gson.fromJson(jsonObject.toString(),User.class);
                                    setUseInfo(user);
                                }else {
                                    Toast.makeText(MainActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(MainActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(MainActivity.this, "获取失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

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
        mTextRightTitle=findViewById(R.id.tv_right_title);
        mTextRightTitle.setVisibility(View.GONE);
    }
    private void setUseInfo(User user) {
        if(user!=null){
            status=user.getVerificationStatus();

            if(user.getVerificationStatus()==0){
                //未认证
                showRzRusultView("用户未认证,请进行实名认证！");

            }else if(user.getVerificationStatus()==3){
                //认证失败
                showRzRusultView("用户认证未通过,请重新进行实名认证！");
            }

        }
    }

    private void showRzRusultView(String msg){
        final ExitDialogFragment dialogFragment=ExitDialogFragment.getInstance(msg);
        dialogFragment.showF(getSupportFragmentManager(),"showRzRusultView");
        dialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                dialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                dialogFragment.dismissAllowingStateLoss();
                Intent intent=new Intent(MainActivity.this,UserRzActivity.class);
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
                Intent mIntent=new Intent(MainActivity.this, StartCarRecordActivity.class);
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
