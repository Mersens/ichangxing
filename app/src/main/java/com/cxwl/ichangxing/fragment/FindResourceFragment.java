package com.cxwl.ichangxing.fragment;

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
import com.cxwl.ichangxing.adapter.FindResourceAdapter;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.GrabParamsEntity;
import com.cxwl.ichangxing.entity.MsgNoticeEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.cxwl.ichangxing.view.RecyclerViewNoBugLinearLayoutManager;
import com.google.gson.Gson;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;

public class FindResourceFragment extends BaseFragment {
    private CityPickerView mStartCityPickerView = new CityPickerView();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout mLayoutNoMsg;
    private RecyclerView recyclerView;
    private static final int pageSize = 10;
    private int pageCurrent = 1;
    private TextView mTextReClick;
    private RelativeLayout mLayoutStart;
    private RelativeLayout mLayoutEnd;
    private TextView mTextStart;

    private ImageView mImgSearch;
    private String startArea=null;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean isLoadMoreEmpty = false;
    private boolean isFirst = true;
    private boolean isSearch=false;
    List<GrabParamsEntity> mList=new ArrayList<>();
    GrabParamsEntity entity=new GrabParamsEntity();
    private FindResourceAdapter adapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_find_resource;
    }

    @Override
    protected void initView(View view) {
        mLayoutStart=view.findViewById(R.id.layout_start);
        mLayoutEnd=view.findViewById(R.id.layout_end);
        mTextStart=view.findViewById(R.id.tv_start);
        mImgSearch=view.findViewById(R.id.img_search);
        mTextReClick=view.findViewById(R.id.btn_reclick);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        int color = getResources().getColor(R.color.actionbar_color);
        swipeRefreshLayout.setColorSchemeColors(color, color, color);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null);
        adapter=new FindResourceAdapter(getActivity(),mList);
        recyclerView.setAdapter(adapter);
        mLayoutNoMsg=view.findViewById(R.id.layout_nomsg);
        mLayoutNoMsg.setVisibility(View.GONE);
        initCityPicker();
        initEvent();

    }

    private void doQD(final GrabParamsEntity entity){
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getChildFragmentManager(),"doQDView");

        RequestManager.getInstance()
                .mServiceStore
                .grabPallet(token,entity.getPalletId(),"","","")
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
                                    RxBus.getInstance().send(new EventEntity(EventEntity.WAYBILL_INFO_REFRESH));
                                    RxBus.getInstance().send(new EventEntity(EventEntity.NOTICE_MSG));
                                    Toast.makeText(getActivity(), "抢单成功！", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "抢单失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(getActivity(), "抢单失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }


    private void initEvent() {
        adapter.setOnButtonClickListener(new FindResourceAdapter.OnButtonClickListener() {
            @Override
            public void onClick(GrabParamsEntity entity) {
                doQD(entity);
            }
        });
        mLayoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartCityPickerView.showCityPicker();
            }
        });

        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();

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

    private void refreshDatas() {
        mList.clear();
        adapter.setDatas(mList);
        isRefresh = true;
        isLoadMore = false;
        isLoadMoreEmpty = false;
        pageCurrent = 1;
        initData();
    }

    private void doSearch() {
        isSearch=true;
        refreshDatas();
    }

    private void initCityPicker() {
        mStartCityPickerView.init(getActivity());
        CityConfig cityConfig = new CityConfig.Builder().title("选择城市")
                .title("选择城市")//标题
                .confirTextColor("#298cf5")//确认按钮文字颜色
                .provinceCyclic(false)//省份滚轮是否可以循环滚动
                .cityCyclic(false)//城市滚轮是否可以循环滚动
                .districtCyclic(false)//区县滚轮是否循环滚动
                .province("河南省")//默认显示的省份
                .city("南阳市")//默认显示省份下面的城市
                .district("卧龙区")//默认显示省市下面的区县数据
                .build();

        mStartCityPickerView.setConfig(cityConfig);
        mStartCityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                StringBuffer sbf=new StringBuffer();
                if (province != null) {
                    String strCity=province.getName();
                    sbf.append(strCity+"-");
                }

                if (city != null) {
                    String strCity=city.getName();
                    sbf.append(strCity+"-");
                }

                if (district != null) {
                    String strdistrict=district.getName();
                    sbf.append(strdistrict);

                }
                entity.setAddr(sbf.toString().replaceAll("-",""));
                mTextStart.setText(sbf.toString());
            }

            @Override
            public void onCancel() {

            }
        });

    }

    @Override
    protected void initData() {
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        JSONObject object=new JSONObject();
        Log.e("RequestBody",object.toString());
        if(!TextUtils.isEmpty(entity.getAddr())){
            try {
                object.put("addr",entity.getAddr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), object.toString());
        RequestManager.getInstance()
                .mServiceStore
                .getGrabInfo(token,pageCurrent+"",pageSize+"",body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        if(swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Log.e("getGrabList", "getGrabList==" + msg);
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
                                        if(isRefresh || isSearch){
                                            isRefresh=false;
                                            isSearch=false;
                                            mList.clear();
                                            adapter.setDatas(mList);
                                        }
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
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(getActivity(), "查询失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    private void analysisJson(JSONArray array) throws JSONException {

        Gson gson=new Gson();
        List<GrabParamsEntity> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String str = array.getString(i);
            GrabParamsEntity entity=gson.fromJson(str,GrabParamsEntity.class);
            list.add(entity);
        }
        mList.addAll(list);
        adapter.setDatas(mList);

    }

    public static FindResourceFragment newInstance(){
        return new FindResourceFragment();
    }

}
