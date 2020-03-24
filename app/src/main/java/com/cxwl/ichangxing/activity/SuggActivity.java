package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxwl.ichangxing.R;

public class SuggActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextRightTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sugg_list);
        init();
    }

    @Override
    public void init() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("投诉咨询");
        mTextRightTitle=findViewById(R.id.tv_right_title);
        mTextRightTitle.setText("添加");
        initEvent();
    }

    private void initEvent() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        mTextRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(SuggActivity.this,AddSuggActivity.class);
                startActivity(mIntent);
               overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }
}
