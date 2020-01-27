package com.cxwl.ichangxing.activity;

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
import com.cxwl.ichangxing.adapter.MsgNoticeAdapter;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.MsgNoticeEntity;
import com.cxwl.ichangxing.entity.OftenLineEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.cxwl.ichangxing.view.MsgMenuFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class MsgCenterActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout mLayoutNoMsg;
    private RecyclerView recyclerView;
    private static final int pageSize = 10;
    private int pageCurrent = 1;
    private TextView mTextReClick;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean isLoadMoreEmpty = false;
    private boolean isFirst = true;
    private List<MsgNoticeEntity> mList=new ArrayList<>();
    MsgNoticeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_msg_center);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("消息中心");
        mTextReClick=findViewById(R.id.btn_reclick);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        int color = getResources().getColor(R.color.actionbar_color);
        swipeRefreshLayout.setColorSchemeColors(color, color, color);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        adapter=new MsgNoticeAdapter(this,mList);
        recyclerView.setAdapter(adapter);
        mLayoutNoMsg=findViewById(R.id.layout_nomsg);
        mLayoutNoMsg.setVisibility(View.GONE);
    }


    private void showMsgMenuView(final MsgNoticeEntity entity){
        final MsgMenuFragment msgMenuFragment=MsgMenuFragment.getInstance();
        msgMenuFragment.showF(getSupportFragmentManager(),"showMsgMenuView");
        msgMenuFragment.setOnDialogClickListener(new MsgMenuFragment.OnDialogClickListener() {
            @Override
            public void onSelect(int readStatus) {
                saveStatus(entity,readStatus);
            }
        });

    }


    private void  saveStatus(MsgNoticeEntity entity,int readStatus){
        String token= SPreferenceUtil.getInstance(this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment= LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"saveStatus");
        RequestManager.getInstance()
                .mServiceStore
                .saveNotice(token,entity.getNoticeId(),readStatus)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("findAccept", "findAccept==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    RxBus.getInstance().send(new EventEntity(EventEntity.NOTICE_MSG));
                                    refreshDatas();
                                    Toast.makeText(MsgCenterActivity.this, "成功！", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MsgCenterActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(MsgCenterActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("findAccept", "findAccept==" + msg);
                        Toast.makeText(MsgCenterActivity.this, "失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    private void delMsg(MsgNoticeEntity entity){
        String token= SPreferenceUtil.getInstance(this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment= LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"delMsg");
        RequestManager.getInstance()
                .mServiceStore
                .deleted(token,entity.getNoticeId())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("findAccept", "findAccept==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    RxBus.getInstance().send(new EventEntity(EventEntity.NOTICE_MSG));
                                    refreshDatas();
                                    Toast.makeText(MsgCenterActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MsgCenterActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(MsgCenterActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("findAccept", "findAccept==" + msg);
                        Toast.makeText(MsgCenterActivity.this, "删除失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }
    private void initEvent() {
        adapter.setOnMsgClickListener(new MsgNoticeAdapter.OnMsgClickListener() {
            @Override
            public void onClick(MsgNoticeEntity entity) {

                int readStatus=entity.getReadStatus();
                if(readStatus==0){
                    showMsgMenuView(entity);
                }
            }
            @Override
            public void onDel(MsgNoticeEntity entity) {
                delMsg(entity);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDatas();
            }
        });
        mTextReClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutNoMsg.setVisibility(View.GONE);
                isFirst = true;
                refreshDatas();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 3) && isSlidingToLast) {
                        //加载更多功能的代码
                        isLoadMore = true;
                        if (!isLoadMoreEmpty) {
                            pageCurrent = pageCurrent + 1;
                            initData();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }

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
    private void refreshDatas() {
        mList.clear();
        isRefresh = true;
        isLoadMore = false;
        isLoadMoreEmpty = false;
        pageCurrent = 1;
        initData();
    }


    protected void initData() {
        String token= SPreferenceUtil.getInstance(this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        AssignParamsEntity entity=new AssignParamsEntity();
        entity.setAcceptStatus(1);
        final Gson gson=new Gson();
        Log.e("token",token);
        Log.e("RequestBody",gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .findNotice(token,pageCurrent+"",pageSize+"")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Log.e("findAccept", "findAccept==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    JSONArray array=jsonObject.getJSONArray("list");
                                    if(array.length()==0){
                                        if (isFirst) {
                                            mLayoutNoMsg.setVisibility(View.VISIBLE);
                                            isFirst = false;
                                        }
                                        if (isLoadMore) {
                                            isLoadMore = false;
                                            isLoadMoreEmpty = true;
                                        }
                                        isLoadMoreEmpty = true;
                                        return;
                                    }else {
                                        isFirst = false;
                                        mLayoutNoMsg.setVisibility(View.GONE);
                                        analysisJson(array);
                                    }

                                }else {
                                    Toast.makeText(MsgCenterActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(MsgCenterActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Log.e("findAccept", "findAccept==" + msg);
                        Toast.makeText(MsgCenterActivity.this, "查询失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void analysisJson(JSONArray array) throws JSONException {
        Gson gson=new Gson();
        List<MsgNoticeEntity> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String str = array.getString(i);
            MsgNoticeEntity entity=gson.fromJson(str,MsgNoticeEntity.class);
            list.add(entity);
        }
        mList.addAll(list);
        adapter.setDatas(mList);
    }
}
