package com.cxwl.ichangxing.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class OrderDetailsActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private TextView mTextDDH;
    private TextView mTextHWMC;
    private TextView mTextDJ;
    private TextView mTextZZL;
    private TextView mTextZHSJ;
    private TextView mTextBZ;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private LinearLayout mLayoutAddress;
    private RelativeLayout mLayoutZHBD;
    private RelativeLayout mLayoutXHBD;
    private RelativeLayout mLayoutYCPZ;
    private Button mBtnWC;
    private Button mBtnHJKF;
    private AssignParamsEntity assignParamsEntity;
    private String acceptId;
    private ImageView mImgZHWC;
    private ImageView mImgXHWC;
    private ImageView mImgYCWC;
    private boolean ispickup=false;
    private boolean isUnload=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_details);
        init();
    }

    @Override
    public void init() {
        initViews();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initViews() {
        acceptId=getIntent().getStringExtra("acceptId");
        mImgBack=findViewById(R.id.imgBack);
        mTextTitle=findViewById(R.id.tv_title);
        mTextTitle.setText("发车");
        mTextDDH=findViewById(R.id.tv_ddh);
        mTextHWMC=findViewById(R.id.tv_hwmc);
        mTextDJ=findViewById(R.id.tv_dj);
        mTextZZL=findViewById(R.id.tv_zzl);
        mTextZHSJ=findViewById(R.id.tv_zhsj);
        mTextBZ=findViewById(R.id.tv_hwbz);
        mLayoutAddress=findViewById(R.id.layout_address);

        mBtnWC=findViewById(R.id.btnwc);
        mLayoutZHBD=findViewById(R.id.layout_zhbz);
        mLayoutXHBD=findViewById(R.id.layout_xhbz);
        mLayoutYCPZ=findViewById(R.id.layout_ycpz);
        mBtnHJKF=findViewById(R.id.btncell);
        mImgXHWC=findViewById(R.id.img_xh_wc);
        mImgZHWC=findViewById(R.id.img_zh_wc);
        mImgYCWC=findViewById(R.id.img_yc_wc);
    }

    private void initEvent() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        mBtnWC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWC();

            }
        });
        mBtnHJKF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(OrderDetailsActivity.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(OrderDetailsActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {
                        callPhone();
                    }
                }else {
                    callPhone();
                }

            }
        });
        mLayoutZHBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailsActivity.this,PickUpActivity.class);
                intent.putExtra("trackId",assignParamsEntity.getTrackId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        mLayoutXHBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailsActivity.this,UnLoadActivity.class);
                intent.putExtra("trackId",assignParamsEntity.getTrackId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        mLayoutYCPZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailsActivity.this,AbnormalActivity.class);
                intent.putExtra("trackId",assignParamsEntity.getTrackId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    private void doWC() {
        if(!ispickup){
            Toast.makeText(this, "请上传装货信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isUnload){
            Toast.makeText(this, "请上传卸货信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        String token= SPreferenceUtil.getInstance(OrderDetailsActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(OrderDetailsActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"orderFinish");
        Log.e("token",token);
        Log.e("acceptId",acceptId);
        RequestManager.getInstance()
                .mServiceStore
                .orderFinish(token,assignParamsEntity.getAcceptId())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("orderFinish", "orderFinish==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    RxBus.getInstance().send(new EventEntity(EventEntity.START_CAR_REFRESH));
                                    Toast.makeText(OrderDetailsActivity.this, "运单已完成！", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                }else {
                                    Toast.makeText(OrderDetailsActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(OrderDetailsActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(OrderDetailsActivity.this, "失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));






    }

    private void callPhone() {
        confirmUserPhone("确定拨打客服电话？");
    }

    private void confirmUserPhone(String s) {
        final ExitDialogFragment dialog = ExitDialogFragment.getInstance(s);
        dialog.show(getSupportFragmentManager(), "CallDialog");
        dialog.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                dialog.dismiss();
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onClickOk() {
                final String num= Constants.SERVICE_MOBILE;
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + num);
                intent.setData(data);
                startActivity(intent);
                dialog.dismiss();
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhone();
            } else
            {
                // Permission Denied
                Toast.makeText(OrderDetailsActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void initDatas() {
        String token= SPreferenceUtil.getInstance(OrderDetailsActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(OrderDetailsActivity.this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("token",token);
        Log.e("acceptId",acceptId);
        RequestManager.getInstance()
                .mServiceStore
                .orderDetail(token,acceptId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("drive", "drive==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    Gson gson=new Gson();
                                    AssignParamsEntity entity=gson.fromJson(jsonObject.toString(),AssignParamsEntity.class);
                                    setDatas(entity);

                                }else {
                                    Toast.makeText(OrderDetailsActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(OrderDetailsActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(OrderDetailsActivity.this, "查询失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void setDatas(AssignParamsEntity entity) {
        assignParamsEntity=entity;
        mTextDDH.setText(entity.getOrderNo());
        mTextHWMC.setText(entity.getCommodityName());
        mTextDJ.setText(entity.getUnitprice()+"元/吨");
        mTextZZL.setText(entity.getTotalWeight()+"吨");
        mTextZHSJ.setText(entity.getLoadDate());
        mTextBZ.setText(entity.getRemark());
        List<AssignParamsEntity.LoadAddrBosBean> loadAddrs=entity.getLoadAddrBos();
        mLayoutAddress.removeAllViews();
        if(loadAddrs!=null){
            if(loadAddrs.size()>0){
                for (int j=0;j<loadAddrs.size();j++){
                    AssignParamsEntity.LoadAddrBosBean bosBean=loadAddrs.get(j);
                    mLayoutAddress.addView(getAddrView(bosBean.getFullAddr(),bosBean.getLatitude(),bosBean.getLongitude(),"装货地址"+(j+1),bosBean.getFullAddr(),
                            "联系人：",bosBean.getContractPerson()+"("+bosBean.getMobile()+")"));
                }
            }
        }
        List<AssignParamsEntity.ReceiveAddrBosBean> receiveAddr=entity.getReceiveAddrBos();
        if(receiveAddr!=null){
            if(receiveAddr.size()>0){
                for (int K=0;K<receiveAddr.size();K++){
                    AssignParamsEntity.ReceiveAddrBosBean receiveBean=receiveAddr.get(K);
                    mLayoutAddress.addView(getAddrView(receiveBean.getFullAddr(),receiveBean.getLatitude(),receiveBean.getLongitude(),"卸货地址"+(K+1),receiveBean.getFullAddr(),
                            "联系人：",receiveBean.getContractPerson()+"("+receiveBean.getMobile()+")"));
                }
            }
        }
        List<AssignParamsEntity.TrackStepBosBean> trackStepBos=entity.getTrackStepBos();
        for (int i = 0; i <trackStepBos.size() ; i++) {
            AssignParamsEntity.TrackStepBosBean bean=trackStepBos.get(i);
            if(bean.getCode().equals("load") && bean.isStatus()){
                mImgZHWC.setVisibility(View.VISIBLE);
                ispickup=true;
            }
            if(bean.getCode().equals("unload") && bean.isStatus()){
                mImgXHWC.setVisibility(View.VISIBLE);
                isUnload=true;
            }
            if(bean.getCode().equals("exception") && bean.isStatus()){
                mImgYCWC.setVisibility(View.VISIBLE);
            }

        }


    }

    private View getAddrView(String addrs,double lat,double lon,String addrkey,String addrvalue,String nameKey,String ameValue){
        LayoutInflater inflater=LayoutInflater.from(OrderDetailsActivity.this);
        View v=inflater.inflate(R.layout.layout_addr_details_view,null);
        TextView mTextAddresKey=v.findViewById(R.id.address);
        mTextAddresKey.setText(addrkey);
        TextView mTextAddresValue=v.findViewById(R.id.tv_address);
        mTextAddresValue.setText(addrvalue);
        ImageView mImgMap=v.findViewById(R.id.img_map);
        mImgMap.setOnClickListener(new MyClickListener(addrs,lat,lon));
        TextView mTextNameKey=v.findViewById(R.id.name);
        mTextNameKey.setText(nameKey);
        TextView mTextNameValue=v.findViewById(R.id.tv_name);
        mTextNameValue.setText(ameValue);
        return v;
    }


    private class MyClickListener implements View.OnClickListener{
        private double lat;
        private double lon;
        private String addrs;
       public MyClickListener(String addrs,double lat,double lon){
           this.lat=lat;
           this.lon=lon;
           this.addrs=addrs;
       }
        @Override
        public void onClick(View v) {
            LatLng p2 = new LatLng(lat, lon);
            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi(addrs, p2, ""), AmapNaviType.DRIVER), null);

        }
    }


}
