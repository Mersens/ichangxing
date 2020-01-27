package com.cxwl.ichangxing.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.StatisticsEntity;
import com.cxwl.ichangxing.entity.TrackLoadEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class IncomeStatisticsActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextZ_JDS;
    private TextView mTextZ_SR;
    private TextView mTextY_JDS;
    private TextView mTextY_SR;
    private TextView mTextJ_JDS;
    private TextView mTextJ_SR;
    private TextView mTextN_JDS;
    private TextView mTextN_SR;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_income_statistic);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();
    }

    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("收入统计");
        mTextZ_JDS = findViewById(R.id.tv_z_jds);
        mTextZ_SR = findViewById(R.id.tv_z_sr);
        mTextY_JDS = findViewById(R.id.tv_y_jds);
        mTextY_SR = findViewById(R.id.tv_y_sr);
        mTextJ_JDS = findViewById(R.id.tv_j_jds);
        mTextJ_SR = findViewById(R.id.tv_j_sr);
        mTextN_JDS = findViewById(R.id.tv_n_jds);
        mTextN_SR = findViewById(R.id.tv_n_sr);
        initData();

    }

    private void initData() {
        String token= SPreferenceUtil.getInstance(IncomeStatisticsActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(IncomeStatisticsActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestManager.getInstance()
                .mServiceStore
                .incomeSt(token)
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
                                    StatisticsEntity entity=gson.fromJson(jsonObject.toString(),StatisticsEntity.class);

                                    if(null!=entity){
                                        mTextZ_JDS.setText(entity.getWeekNum()+"");
                                        mTextZ_SR.setText(entity.getWeekAmount()+"");
                                        mTextY_JDS.setText(entity.getMonthNum()+"");
                                        mTextY_SR.setText(entity.getMonthAmount()+"");
                                        mTextJ_JDS.setText(entity.getSeasonNum()+"");
                                        mTextJ_SR.setText(entity.getSeasonAmount()+"");
                                        mTextN_JDS.setText(entity.getYearNum()+"");
                                        mTextN_SR.setText(entity.getYearAmount()+"");
                                    }


                                }else {
                                    Toast.makeText(IncomeStatisticsActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(IncomeStatisticsActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(IncomeStatisticsActivity.this, "获取失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));


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
