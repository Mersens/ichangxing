package com.cxwl.ichangxing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.activity.OrderDetailsActivity;
import com.cxwl.ichangxing.activity.PickUpActivity;
import com.cxwl.ichangxing.activity.ReceiptActivity;
import com.cxwl.ichangxing.adapter.RecordAdapter;
import com.cxwl.ichangxing.adapter.WaybillAdapter;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.OrderBoEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.RecyclerViewNoBugLinearLayoutManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class RecordWeekFragment extends BaseFragment {
    private CompositeDisposable mCompositeDisposable;

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
    private boolean isSearch=false;
    List<OrderBoEntity> mList=new ArrayList<>();
    private String terms;
    private RecordAdapter adapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_assign_yjj;
    }

    @Override
    protected void initView(View view) {
        terms=getArguments().getString("terms");
        mTextReClick=view.findViewById(R.id.btn_reclick);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        int color = getResources().getColor(R.color.actionbar_color);
        swipeRefreshLayout.setColorSchemeColors(color, color, color);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null);
        adapter=new RecordAdapter(getActivity(),mList);
        recyclerView.setAdapter(adapter);
        mLayoutNoMsg=view.findViewById(R.id.layout_nomsg);
        mLayoutNoMsg.setVisibility(View.GONE);
        initEvent();
        initBus();

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
                            if (type==EventEntity.RECORD_REFRESH) {
                                refreshDatas();
                            }
                        }
                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        mCompositeDisposable.add(d);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void initEvent() {
        adapter.setOnButtonClickListener(new RecordAdapter.OnButtonClickListener() {
            @Override
            public void onAcceptClick(OrderBoEntity entity) {
                Intent intent=new Intent(getActivity(), ReceiptActivity.class);
                intent.putExtra("addr",entity.getReceiptAddress());
                intent.putExtra("orderNo",entity.getOrderNo());
                intent.putExtra("expressName",entity.getExpressName());
                intent.putExtra("expressNo",entity.getExpressNo());
                intent.putExtra("url",entity.getExpressImg());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
                isFirst=true;
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
    }

    private void refreshDatas() {
        mList.clear();
        adapter.setDatas(mList);
        isRefresh = true;
        isLoadMore = false;
        isLoadMoreEmpty = false;
        pageCurrent = 1;
        initData();
    }
    @Override
    protected void initData() {
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        swipeRefreshLayout.setRefreshing(true);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), new JSONObject().toString());
        RequestManager.getInstance()
                .mServiceStore
                .driveList(token,pageCurrent+"",pageSize+"",terms,body)
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
                                        if(isRefresh){
                                            isRefresh=false;
                                            mList.clear();
                                            adapter.setDatas(mList);
                                        }
                                        isLoadMoreEmpty = true;
                                        return;
                                    }else {
                                        isFirst = false;
                                        mLayoutNoMsg.setVisibility(View.GONE);
                                        analysisJson(array);
                                    }

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Log.e("findAccept", "onError==" + msg);
                        Toast.makeText(getActivity(), "查询失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void analysisJson(JSONArray array) throws JSONException {
        Gson gson=new Gson();
        List<OrderBoEntity> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String str = array.getString(i);
            OrderBoEntity entity=gson.fromJson(str,OrderBoEntity.class);
            list.add(entity);
        }
        mList.addAll(list);
        adapter.setDatas(mList);
    }



    public static RecordWeekFragment newInstance(String terms){
        RecordWeekFragment fragment=new RecordWeekFragment();
        Bundle bundle=new Bundle();
        bundle.putString("terms",terms);
        fragment.setArguments(bundle);
        return fragment;
    }
}
