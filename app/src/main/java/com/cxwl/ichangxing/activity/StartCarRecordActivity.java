package com.cxwl.ichangxing.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.adapter.OrderPagerAdapter;
import com.cxwl.ichangxing.fragment.RecordWeekFragment;
import com.cxwl.ichangxing.view.OrderViewPager;

import java.util.ArrayList;
import java.util.List;

public class StartCarRecordActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TabLayout mTabLayout;
    private OrderViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> tabTitles;
    private OrderPagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_startcar_record);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager =  findViewById(R.id.viewPager);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(RecordWeekFragment.newInstance("0"));
        fragmentList.add(RecordWeekFragment.newInstance("1"));
        tabTitles = new ArrayList<String>();
        tabTitles.add("一周");
        tabTitles.add("三个月");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(1)));
        mAdapter = new OrderPagerAdapter(getSupportFragmentManager(),
                fragmentList, tabTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }



    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("发车记录");

    }

    private void initEvent() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }



}
