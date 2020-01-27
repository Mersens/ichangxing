package com.cxwl.ichangxing.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.DedicatedLineBoEntity;
import com.cxwl.ichangxing.entity.OftenLineEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener;
import com.lljjcoder.bean.CustomCityData;
import com.lljjcoder.citywheel.CustomConfig;
import com.lljjcoder.style.citycustome.CustomCityPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class AddOftenLineActivity extends BaseActivity {
    private CustomCityPicker startCityPicker = null;
    private CustomCityPicker endCityPicker = null;
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextStartAddress;
    private TextView mTextEndAddress;
    private ImageView mImgStart;
    private ImageView mImgEnd;
    private Button mBtnSave;
    private List<CustomCityData> mList=new ArrayList<>();
    public CustomConfig.WheelType mWheelType = CustomConfig.WheelType.PRO_CITY;
    DedicatedLineBoEntity dedicatedLineBoEntity=new DedicatedLineBoEntity();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_often_line);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();
        initDatas();


    }

    private void initDatas() {
        String token= SPreferenceUtil.getInstance(AddOftenLineActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        getProvinceInfo(token);
    }

    private void getProvinceInfo(final String token) {
        RequestManager.getInstance()
                .mServiceStore
                .place(token,"")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //成功
                                    JSONArray array=object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject=array.getJSONObject(i);
                                        String id=jsonObject.getString("id");
                                        String name=jsonObject.getString("name");
                                        CustomCityData customCityData=new CustomCityData(id,name);
                                        getCityInfo(token,id,customCityData);
                                    }



                                }else {
                                    Toast.makeText(AddOftenLineActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(AddOftenLineActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(AddOftenLineActivity.this, "失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }



    private void getCityInfo(String token,String id,final CustomCityData customCityData){
        RequestManager.getInstance()
                .mServiceStore
                .place(token,id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //成功

                                    List<CustomCityData> list=new ArrayList<>();
                                    JSONArray array=object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject=array.getJSONObject(i);
                                        String id=jsonObject.getString("id");
                                        String name=jsonObject.getString("name");
                                        CustomCityData cityData=new CustomCityData(id,name);
                                        list.add(cityData);
                                    }
                                    customCityData.setList(list);
                                    mList.add(customCityData);
                                    setCityDatas();
                                }else {
                                    Toast.makeText(AddOftenLineActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(AddOftenLineActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(AddOftenLineActivity.this, "失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void setCityDatas() {
        CustomConfig cityConfig = new CustomConfig.Builder()
                .title("选择城市")
                .visibleItemsCount(5)
                .setCityData(mList)//设置数据源
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .setCityWheelType(mWheelType)
                .build();
        startCityPicker = new CustomCityPicker(AddOftenLineActivity.this);
        endCityPicker = new CustomCityPicker(AddOftenLineActivity.this);
        startCityPicker.setCustomConfig(cityConfig);
        endCityPicker.setCustomConfig(cityConfig);
        initCityPicker();
    }

    private void initCityPicker() {
        startCityPicker.setOnCustomCityPickerItemClickListener(new OnCustomCityPickerItemClickListener() {
            @Override
            public void onSelected(CustomCityData province, CustomCityData city, CustomCityData district) {
                super.onSelected(province, city, district);
                StringBuffer sbf=new StringBuffer();
                if(province!=null){
                    dedicatedLineBoEntity.setOprovince(province.getId());
                    sbf.append(province.getName());

                }
                if(city!=null){
                    sbf.append("-");
                    sbf.append(city.getName());
                    dedicatedLineBoEntity.setOriginId(city.getId());
                }
                dedicatedLineBoEntity.setOrigin(sbf.toString());
                mTextStartAddress.setText(sbf.toString());
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });
        endCityPicker.setOnCustomCityPickerItemClickListener(new OnCustomCityPickerItemClickListener() {
            @Override
            public void onSelected(CustomCityData province, CustomCityData city, CustomCityData district) {
                super.onSelected(province, city, district);
                StringBuffer sbf=new StringBuffer();
                if(province!=null){
                    sbf.append(province.getName());
                    dedicatedLineBoEntity.setDprovince(province.getId());
                }
                if(city!=null){
                    sbf.append("-");
                    sbf.append(city.getName());
                    dedicatedLineBoEntity.setDestinationId(city.getId());
                }
                dedicatedLineBoEntity.setDestination(sbf.toString());
                mTextEndAddress.setText(sbf.toString());
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });
    }

    private void initView() {
        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("添加常跑路线");
        mTextStartAddress=findViewById(R.id.tv_start);
        mTextEndAddress=findViewById(R.id.tv_end);
        mImgStart=findViewById(R.id.img_start);
        mImgEnd=findViewById(R.id.img_end);
        mBtnSave=findViewById(R.id.btnsave);
    }

    private void initEvent() {
        mImgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCityPicker.showCityPicker();
            }
        });
        mImgEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCityPicker.showCityPicker();

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
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });


    }

    private void doSave() {

        if(TextUtils.isEmpty(dedicatedLineBoEntity.getDestination())){
            Toast.makeText(this, "请选择终点位置", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(dedicatedLineBoEntity.getOrigin())){
            Toast.makeText(this, "请选择起点位置", Toast.LENGTH_SHORT).show();

            return;
        }
        String token= SPreferenceUtil.getInstance(AddOftenLineActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("token",token);
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"addOftenLineLoading");
        final Gson gson=new Gson();
        dedicatedLineBoEntity.setId("");
        Log.e("RequestBody",gson.toJson(dedicatedLineBoEntity));
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(dedicatedLineBoEntity));
        RequestManager.getInstance()
                .mServiceStore
                .oftenLineSave(token,body)
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
                                    //添加成功
                                    Toast.makeText(AddOftenLineActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                                }else {
                                    Toast.makeText(AddOftenLineActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(AddOftenLineActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(AddOftenLineActivity.this, "添加失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }
}
