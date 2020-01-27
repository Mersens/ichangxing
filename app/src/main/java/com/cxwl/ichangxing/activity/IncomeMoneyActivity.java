package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.adapter.OrderPagerAdapter;
import com.cxwl.ichangxing.entity.StatisticsEntity;
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.fragment.SQFragment;
import com.cxwl.ichangxing.fragment.TXFragment;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.TXViewFragment;
import com.cxwl.ichangxing.view.ViewPagerForScrollView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class IncomeMoneyActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextZHYE;
    private TextView mTextKTXE;
    private RelativeLayout mLayoutTX;
    private RelativeLayout mLayoutSRTJ;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> tabTitles;
    private OrderPagerAdapter mAdapter;
    User entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_income_money);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initDatas() {
        String token= SPreferenceUtil.getInstance(IncomeMoneyActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(IncomeMoneyActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        getUserInfo(token);

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
                                    //成功
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    Gson gson=new Gson();
                                     entity=gson.fromJson(jsonObject.toString(),User.class);
                                     if(null!=entity){
                                         mTextZHYE.setText(entity.getBalance()+" 元");
                                         mTextKTXE.setText(entity.getWithdraw()+" 元");
                                     }

                                }else {
                                    Toast.makeText(IncomeMoneyActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(IncomeMoneyActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(IncomeMoneyActivity.this, "获取失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("收钱记录");
        mTextZHYE = findViewById(R.id.tv_zhye);
        mTextKTXE = findViewById(R.id.tv_ktxe);
        mLayoutTX = findViewById(R.id.layout_tx);
        mLayoutSRTJ = findViewById(R.id.layout_srtj);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(1);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(TXFragment.newInstance());
        fragmentList.add(SQFragment.newInstance());
        tabTitles = new ArrayList<String>();
        tabTitles.add("提现记录");
        tabTitles.add("收钱记录");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(1)));
        mAdapter = new OrderPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initEvent() {
        mLayoutTX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(entity.getBankAccount())){
                    showBindBankView("请先绑定银行卡！");
                    return;
                }else {
                    showTXView();
                    //提现
                }

            }
        });
        mLayoutSRTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(IncomeMoneyActivity.this,
                        IncomeStatisticsActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    private void showBindBankView(String msg){
        final ExitDialogFragment exitDialogFragment=ExitDialogFragment.getInstance(msg);
        exitDialogFragment.showF(getSupportFragmentManager(),"showBindBankView");
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
                Intent mIntent = new Intent(IncomeMoneyActivity.this,
                        AddBankActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

    private void showTXView() {
        if(null==entity){
            Toast.makeText(this, "用户信息为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        final TXViewFragment fragment=TXViewFragment.getInstance(entity.getWithdraw(),entity.getBankAccount());
        fragment.showF(getSupportFragmentManager(),"TXView");
        fragment.setOnDialogClickListener(new TXViewFragment.OnDialogClickListener() {
            @Override
            public void onSelect(double money) {
                fragment.dismissAllowingStateLoss();
                doTx(money);
            }
        });

    }

    private void doTx(double money) {
        String token= SPreferenceUtil.getInstance(IncomeMoneyActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(IncomeMoneyActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestManager.getInstance()
                .mServiceStore
                .applyWithdraw(token,money+"")
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
                                    //成功
                                    initDatas();
                                    Toast.makeText(IncomeMoneyActivity.this, "提现申请提交成功！", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(IncomeMoneyActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(IncomeMoneyActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(IncomeMoneyActivity.this, "获取失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
