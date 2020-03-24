package com.cxwl.ichangxing.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxwl.ichangxing.R;

public class AddSuggActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sugg_add);
        init();
    }

    @Override
    public void init() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("添加投诉咨询");

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


    }
}
