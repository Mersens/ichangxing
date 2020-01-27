package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.adapter.OftenLineListAdapter;
import com.cxwl.ichangxing.entity.DedicatedLineBoEntity;
import com.cxwl.ichangxing.entity.OftenLineEntity;
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.cxwl.ichangxing.view.RecyclerViewNoBugLinearLayoutManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class OftenLineActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextRightTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout mLayoutNoMsg;
    private RecyclerView recyclerView;
    private TextView mTextReClick;
    private List<DedicatedLineBoEntity> mList=new ArrayList<>();
    private OftenLineListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_often_line);
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
        initData();
    }

    private void initData() {
        mList.clear();
        swipeRefreshLayout.setRefreshing(true);
        String token = SPreferenceUtil.getInstance(this).getToken();
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "token为空，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        RequestManager.getInstance()
                .mServiceStore
                .getOftenLineInfo(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Log.e("getOftenLineInfo", "getOftenLineInfo==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 0) {
                                    //成功
                                    JSONArray array=object.getJSONArray("data");
                                    if(array.length()>0){
                                        mLayoutNoMsg.setVisibility(View.GONE);
                                        analysisJson(array);
                                    }else {
                                        mLayoutNoMsg.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    mLayoutNoMsg.setVisibility(View.VISIBLE);
                                    Toast.makeText(OftenLineActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            mLayoutNoMsg.setVisibility(View.VISIBLE);
                            Toast.makeText(OftenLineActivity.this, "获取信息失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        mLayoutNoMsg.setVisibility(View.VISIBLE);
                        Log.e("getOftenLineInfo", "onError==" + msg);
                        Toast.makeText(OftenLineActivity.this, "获取信息失败！" + msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void analysisJson(JSONArray array) throws JSONException {
        Gson gson=new Gson();
        List<DedicatedLineBoEntity> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String str = array.getString(i);
            DedicatedLineBoEntity entity=gson.fromJson(str,DedicatedLineBoEntity.class);
            list.add(entity);
        }
        mList.addAll(list);
        adapter.setDatas(mList);
    }

    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextRightTitle = findViewById(R.id.tv_right_title);
        mTextTitle.setText("常跑路线");
        mTextRightTitle.setText("添加");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        int color = getResources().getColor(R.color.actionbar_color);
        swipeRefreshLayout.setColorSchemeColors(color, color, color);
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        adapter=new OftenLineListAdapter(this,mList);
        recyclerView.setAdapter(adapter);
        mLayoutNoMsg = findViewById(R.id.layout_nomsg);
        mLayoutNoMsg.setVisibility(View.GONE);
        mTextReClick=findViewById(R.id.btn_reclick);

    }

    private void initEvent() {
        adapter.setOnOftenLineDelListener(new OftenLineListAdapter.OnOftenLineDelListener() {
            @Override
            public void onDel(final DedicatedLineBoEntity entity) {
                showDleOftenLineView(entity);
            }
        });
        mTextReClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutNoMsg.setVisibility(View.GONE);
                refreshDatas();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDatas();
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
        mTextRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加
                Intent mIntent = new Intent(OftenLineActivity.this, AddOftenLineActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    private void showDleOftenLineView(final DedicatedLineBoEntity entity) {
        String msg="确定删除该路线？";
        final ExitDialogFragment dialogFragment=ExitDialogFragment.getInstance(msg);
        dialogFragment.showF(getSupportFragmentManager(),"delOftenLineView");
        dialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                dialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                dialogFragment.dismissAllowingStateLoss();
                delOftenLine(entity);
            }
        });

    }

    private void delOftenLine(DedicatedLineBoEntity entity){
        String token=SPreferenceUtil.getInstance(OftenLineActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"delOftenLineLoading");
        final Gson gson=new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .oftenLineDel(token,body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "oftenLineDel==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //删除成功
                                    refreshDatas();

                                }else {
                                    Toast.makeText(OftenLineActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(OftenLineActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(OftenLineActivity.this, "删除失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }



    private void refreshDatas() {
        initData();
    }
}
