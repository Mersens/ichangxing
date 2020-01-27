package com.cxwl.ichangxing.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.activity.AbutOursActivity;
import com.cxwl.ichangxing.activity.StartCarRecordActivity;
import com.cxwl.ichangxing.activity.CheckUserRzActivity;
import com.cxwl.ichangxing.activity.IncomeMoneyActivity;
import com.cxwl.ichangxing.activity.LoginActivity;
import com.cxwl.ichangxing.activity.MsgCenterActivity;
import com.cxwl.ichangxing.activity.OftenLineActivity;
import com.cxwl.ichangxing.activity.UpdatePwdActivity;
import com.cxwl.ichangxing.activity.UserRzActivity;
import com.cxwl.ichangxing.app.Constants;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyInfoFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mImgUser;
    private CompositeDisposable mCompositeDisposable;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TextView mTextName;
    private TextView mTextStatus;
    private RelativeLayout mLayoutExit;
    private RelativeLayout mLayoutRZZZ;
    private RelativeLayout mLayoutCPLX;
    private RelativeLayout mLayoutXGMM;
    private RelativeLayout mLayoutGYWM;
    private RelativeLayout mLayoutXXZZ;
    private RelativeLayout mLayoutSQJL;
    private RelativeLayout mLayoutYHK;
    private RelativeLayout mLayoutKF;
    private int status=0;
    private TextView mTextMsgCount;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_myinfo;
    }

    @Override
    protected void initView(View view) {
        mImgUser = view.findViewById(R.id.img_user);
        mTextName = view.findViewById(R.id.tv_user_name);
        mLayoutExit = view.findViewById(R.id.layout_exit);
        mLayoutRZZZ = view.findViewById(R.id.layout_rzzx);
        mLayoutCPLX = view.findViewById(R.id.layout_cplx);
        mLayoutXGMM = view.findViewById(R.id.layout_xgmm);
        mLayoutGYWM = view.findViewById(R.id.layout_gywm);
        mLayoutXXZZ = view.findViewById(R.id.layout_xxzx);
        mLayoutSQJL = view.findViewById(R.id.layout_sqjl);
        mLayoutYHK = view.findViewById(R.id.layout_yhk);
        mTextStatus=view.findViewById(R.id.tv_status);
        mLayoutKF=view.findViewById(R.id.layout_kf);
        mTextMsgCount=view.findViewById(R.id.tv_msg_count);
        mTextName.setText("未登录");
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
                            if (type==EventEntity.NOTICE_MSG) {
                                initData();
                            }else if(type==EventEntity.USER_INFO_REFRESH) {
                                getUserInfo();
                            }
                        }
                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        mCompositeDisposable.add(d);
    }

    private void getUserInfo() {
        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "用户信息为空，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestManager.getInstance()
                .mServiceStore
                .getUserInfo(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("getUserInfo", "getUserInfo==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    SPreferenceUtil.getInstance(getActivity())
                                            .setUserinfo(jsonObject.toString());
                                    Gson gson=new Gson();
                                    User user=gson.fromJson(jsonObject.toString(),User.class);
                                    setUseInfo(user);

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(getActivity(), "获取失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    private void initEvent() {
        mImgUser.setOnClickListener(this);
        mLayoutExit.setOnClickListener(this);
        mLayoutRZZZ.setOnClickListener(this);
        mLayoutCPLX.setOnClickListener(this);
        mLayoutXGMM.setOnClickListener(this);
        mLayoutGYWM.setOnClickListener(this);
        mLayoutXXZZ.setOnClickListener(this);
        mLayoutSQJL.setOnClickListener(this);
        mLayoutYHK.setOnClickListener(this);
        mLayoutKF.setOnClickListener(this);


    }

    @Override
    protected void initData() {
      String userinfo=SPreferenceUtil.getInstance(getActivity()).getUserinfo();
      String token=SPreferenceUtil.getInstance(getActivity()).getToken();
      if(!TextUtils.isEmpty(userinfo) && !TextUtils.isEmpty(token)){
          Gson gson=new Gson();
          User user=gson.fromJson(userinfo,User.class);
          setUseInfo(user);
          getMsgCount(token);
      }else {
          Toast.makeText(getActivity(), "用户信息为空，请重新登录！", Toast.LENGTH_SHORT).show();
          return;
      }
    }

    private void getMsgCount(String token){
        RequestManager.getInstance()
                .mServiceStore
                .noticeCount(token)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {

                        Log.e("noticeCount", "noticeCount==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //查询成功
                                    int count=object.getInt("data");
                                    if(count>0){
                                        mTextMsgCount.setText(count+"");
                                        mTextMsgCount.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        Log.e("noticeCount", "findAccept==" + msg);
                    }
                }));

    }


    private void setUseInfo(User user) {
        if(user!=null){
            status=user.getVerificationStatus();
            mTextName.setText(user.getMobile());

            if(user.getVerificationStatus()==0){
                //未认证
                mTextStatus.setText("未认证");
            }else if(user.getVerificationStatus()==1){
                //认证中
                mTextStatus.setText("认证中");
            }else if(user.getVerificationStatus()==2){
                //认证通过
                mTextStatus.setText("认证通过");
            }else if(user.getVerificationStatus()==3){
                //认证失败
                mTextStatus.setText("认证失败");
            }
        }
    }

    public static MyInfoFragment newInstance() {
        return new MyInfoFragment();
    }


    @Override
    public void onClick(View v) {
        Intent mIntent=null;
        switch (v.getId()){
            case R.id.img_user:
                checkUserStatus();
                break;
            case R.id.layout_exit:
                showExitView();
                break;
            case R.id.layout_rzzx:
                checkUserStatus();
                break;
            case R.id.layout_cplx:
                mIntent=new Intent(getActivity(), OftenLineActivity.class);
                break;
            case R.id.layout_xgmm:
                mIntent=new Intent(getActivity(), UpdatePwdActivity.class);
                break;
            case R.id.layout_gywm:
                mIntent=new Intent(getActivity(), AbutOursActivity.class);
                break;
            case R.id.layout_xxzx:
                mIntent=new Intent(getActivity(), MsgCenterActivity.class);
                break;
            case R.id.layout_sqjl:
                mIntent=new Intent(getActivity(), IncomeMoneyActivity.class);
                break;
            case R.id.layout_yhk:
                mIntent=new Intent(getActivity(), StartCarRecordActivity.class);
                break;
            case R.id.layout_kf:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {
                        callPhone();
                    }
                }else {
                    callPhone();
                }

                break;
        }
        if(null!=mIntent){
            startActivity(mIntent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

    }

    private void callPhone() {
        confirmUserPhone("确定拨打电话？");
    }

    private void confirmUserPhone(String s) {
        final ExitDialogFragment dialog = ExitDialogFragment.getInstance(s);
        dialog.show(getChildFragmentManager(), "CallDialog");
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
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void checkUserStatus() {
        Intent mIntent=null;
        if(status==0){
            //未认证,跳转认证
            mIntent=new Intent(getActivity(), UserRzActivity.class);
        }else if(status==1){
            //认证中
            String msg="认证信息正在审核中...请你耐心等待。";
            showRzView(msg);
        }else if(status==2){
            //认证通过 跳转查看
            mIntent=new Intent(getActivity(), UserRzActivity.class);
        }else if(status==3){
           //认证失败 跳转认证
            mIntent=new Intent(getActivity(), UserRzActivity.class);
        }
        if(null!=mIntent){
            startActivity(mIntent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    private void showRzView(String msg) {
        final ExitDialogFragment exitDialogFragment=ExitDialogFragment.getInstance(msg);
        exitDialogFragment.showF(getChildFragmentManager(),"RzView");
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
            }
        });

    }

    private void showExitView() {
        String msg="确定退出登录？";
        final ExitDialogFragment exitDialogFragment=ExitDialogFragment.getInstance(msg);
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
                doExit();

            }
        });
        exitDialogFragment.showF(getChildFragmentManager(),"exitView");
    }

    private void doExit() {
        final LoadingDialogFragment dialogFragment=LoadingDialogFragment.getInstance();
        dialogFragment.showF(getChildFragmentManager(),"exitLoadView");
        RequestManager.getInstance()
                .mServiceStore
                .logout()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("login", "login==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //成功
                                    SPreferenceUtil.getInstance(getActivity()).setUserId(null);
                                    SPreferenceUtil.getInstance(getActivity()).setUserinfo(null);
                                    SPreferenceUtil.getInstance(getActivity()).setToken(null);
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    getActivity().finish();
                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "退出登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("login", "onError==" + msg);
                        Toast.makeText(getActivity(), "退出登录失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

}
