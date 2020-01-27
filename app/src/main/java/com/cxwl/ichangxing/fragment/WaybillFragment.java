package com.cxwl.ichangxing.fragment;

import android.support.v4.app.Fragment;
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
import com.cxwl.ichangxing.adapter.MenuPageAdapter;
import com.cxwl.ichangxing.adapter.WaybillAdapter;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.GrabParamsEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.CancelDialogFragment;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.cxwl.ichangxing.view.NoScrollViewPager;
import com.cxwl.ichangxing.view.RecyclerViewNoBugLinearLayoutManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class WaybillFragment extends BaseFragment {
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
    List<AssignParamsEntity> mList=new ArrayList<>();
    WaybillAdapter adapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_waybill;
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
        adapter=new WaybillAdapter(getActivity(),mList);
        recyclerView.setAdapter(adapter);
        mLayoutNoMsg=view.findViewById(R.id.layout_nomsg);
        mLayoutNoMsg.setVisibility(View.GONE);
        initEvent();
    }
    private void initEvent() {
        adapter.setOnButtonClickListener(new WaybillAdapter.OnButtonClickListener() {
            @Override
            public void onCancelClick(AssignParamsEntity entity) {
                String msg="";
                int isGrap=entity.getIsGrab();
                if(isGrap==0){
                    msg="是否拒绝接受该运单？";
                    showJJView(entity,msg);
                }else {
                    msg="是否取消该运单";
                    showCancelView(entity,msg);
                }
            }

            @Override
            public void onAcceptClick(AssignParamsEntity entity) {
                acceptOrder(entity);

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


    private void acceptOrder(AssignParamsEntity entity){
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getChildFragmentManager(),"acceptOrderView");
        RequestManager.getInstance()
                .mServiceStore
                .accept(token,entity.getAcceptId(),"","")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("accept", "accept==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    RxBus.getInstance().send(new EventEntity(EventEntity.START_CAR));
                                    refreshDatas();
                                    Toast.makeText(getActivity(), "接单成功！", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "接单失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(getActivity(), "接单失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));




    }

    private void showCancelView(final AssignParamsEntity entity, String msg) {
        final ExitDialogFragment exitDialogFragment=ExitDialogFragment.getInstance(msg);
        exitDialogFragment.showF(getChildFragmentManager(),"showCancelView");
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
                doCancel(entity);

            }
        });

    }

    private void doCancel(AssignParamsEntity entity){
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getChildFragmentManager(),"cancelLoadView");
        RequestManager.getInstance()
                .mServiceStore
                .cancelGrab(token,entity.getAcceptId())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("getGrabList", "getGrabList==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    refreshDatas();
                                    Toast.makeText(getActivity(), "取消成功！", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "取消失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(getActivity(), "取消失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    private void showJJView(final AssignParamsEntity entity, String msg) {
        final ExitDialogFragment exitDialogFragment=ExitDialogFragment.getInstance(msg);
        exitDialogFragment.showF(getChildFragmentManager(),"showJJView");
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
                showAddRemarkView(entity);

            }
        });



    }



    private void showAddRemarkView(final AssignParamsEntity entity){
        final CancelDialogFragment dialogFragment=CancelDialogFragment.getInstance();
        dialogFragment.showF(getChildFragmentManager(),"showJJView");
        dialogFragment.setOnDialogClickListener(new CancelDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickOk(String msg) {
                dialogFragment.dismissAllowingStateLoss();
                doJJ(entity,msg);
            }
        });
    }

    private void doJJ(AssignParamsEntity entity,String reasions){
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getChildFragmentManager(),"jjLoadView");
        RequestManager.getInstance()
                .mServiceStore
                .refuse(token,entity.getAcceptId(),reasions)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("getGrabList", "getGrabList==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    refreshDatas();
                                    Toast.makeText(getActivity(), "拒绝成功！", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "拒绝失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(getActivity(), "拒绝失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

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
    protected void initData() {
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        AssignParamsEntity entity=new AssignParamsEntity();
        entity.setAcceptStatus(0);
        Gson gson=new Gson();
        Log.e("token",token);
        swipeRefreshLayout.setRefreshing(true);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(entity));
        RequestManager.getInstance()
                .mServiceStore
                .findAccept(token,pageCurrent+"",pageSize+"","totalOut",body)
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
        List<AssignParamsEntity> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String str = array.getString(i);
            AssignParamsEntity entity=gson.fromJson(str,AssignParamsEntity.class);
            list.add(entity);
        }
        mList.addAll(list);
        adapter.setDatas(mList);
    }

    public static WaybillFragment newInstance() {
        return new WaybillFragment();
    }






}
