package com.cxwl.ichangxing.fragment;

import android.content.Intent;
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
import com.cxwl.ichangxing.activity.LoginActivity;
import com.cxwl.ichangxing.activity.OrderDetailsActivity;
import com.cxwl.ichangxing.activity.RegisterActivity;
import com.cxwl.ichangxing.adapter.StartCarAdapter;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
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

public class StartFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private CompositeDisposable mCompositeDisposable;

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
    List<AssignParamsEntity> mList=new ArrayList<>();
    StartCarAdapter adapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_start;
    }

    @Override
    protected void initView(View view) {

        mTextReClick=view.findViewById(R.id.btn_reclick);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        int color = getResources().getColor(R.color.actionbar_color);
        swipeRefreshLayout.setColorSchemeColors(color, color, color);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null);
        adapter=new StartCarAdapter(getActivity(),mList);
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
                            if (type==EventEntity.START_CAR_REFRESH) {
                               refreshDatas();
                            }
                        }
                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        mCompositeDisposable.add(d);

    }

    private void startCar(final AssignParamsEntity entity){
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getChildFragmentManager(),"startCarView");
        RequestManager.getInstance()
                .mServiceStore
                .drive(token,entity.getAcceptId())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("drive", "drive==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    refreshDatas();
                                    showOrderDetails(entity);
                                    Toast.makeText(getActivity(), "发车成功！", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "发车失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(getActivity(), "发车失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }


    private void showOrderDetails(AssignParamsEntity entity){
        Intent intent=new Intent(getActivity(), OrderDetailsActivity.class);
        String acceptId=entity.getAcceptId();
        intent.putExtra("acceptId",acceptId);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void initEvent() {
        adapter.setOnButtonClickListener(new StartCarAdapter.OnButtonClickListener() {
            @Override
            public void onAcceptClick(AssignParamsEntity entity) {
                int acceptStatus=entity.getAcceptStatus();
                if(acceptStatus==1){
                   //发车
                    startCar(entity);
                }else if(acceptStatus==3){
                   //详情
                    showOrderDetails(entity);
                }

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
                            Toast.makeText(getActivity(), "正在加载第" + pageCurrent + "页", Toast.LENGTH_SHORT).show();
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
        isRefresh = true;
        isLoadMore = false;
        isLoadMoreEmpty = false;
        pageCurrent = 1;
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    protected void initData() {
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        AssignParamsEntity entity=new AssignParamsEntity();
        entity.setAcceptStatus(3);
        final Gson gson=new Gson();
        Log.e("token",token);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .findAccept(token,pageCurrent+"",pageSize+"","",body)
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
                                    mLayoutNoMsg.setVisibility(View.VISIBLE);

                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            mLayoutNoMsg.setVisibility(View.VISIBLE);

                            Toast.makeText(getActivity(), "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        mLayoutNoMsg.setVisibility(View.VISIBLE);

                        Log.e("findAccept", "findAccept==" + msg);
                        Toast.makeText(getActivity(), "查询失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void analysisJson(JSONArray array) throws JSONException {
        Gson gson=new Gson();
        List<AssignParamsEntity> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String str = array.getString(i);
            AssignParamsEntity entity=gson.fromJson(str,AssignParamsEntity.class);
            list.add(entity);
        }
        mList.addAll(list);
        adapter.setDatas(mList);
    }
    public static StartFragment newInstance(){
        return new StartFragment();
    }


}
