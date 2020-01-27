package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.LoginEntity;
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.utils.SystemUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class UpdatePwdActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private EditText mEditMobile;
    private EditText mEditOldPwd;
    private EditText mEditNewPwd;
    private Button mBtnOk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_pwd);
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
        mTextTitle.setText("修改密码");
        mEditMobile=findViewById(R.id.editPhone);
        mEditNewPwd=findViewById(R.id.editNewPwd);
        mEditOldPwd=findViewById(R.id.editOldPwd);
        mBtnOk=findViewById(R.id.btnok);
        String userInfo= SPreferenceUtil.getInstance(this).getUserinfo();
        if(TextUtils.isEmpty(userInfo)){
            Toast.makeText(this, "用户信息为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        User user=gson.fromJson(userInfo,User.class);
        if(null!=user){
            mEditMobile.setText(user.getMobile());
            mEditMobile.setEnabled(false);
        }
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
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdate();
            }
        });
    }

    private void doUpdate() {
        final String mobile=mEditMobile.getText().toString().trim();
        final String oldPwd=mEditOldPwd.getText().toString().trim();
        final String  newPwd=mEditNewPwd.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)){
            Toast.makeText(this, "手机号为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(oldPwd)){
            Toast.makeText(this, "旧密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(newPwd)){
            Toast.makeText(this, "新密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(oldPwd.equals(newPwd)){
            Toast.makeText(this, "新密码与旧密码一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setDeviceNumber(SystemUtil.getSystemModel());
        loginEntity.setMobile(mobile);
        loginEntity.setPassword(newPwd);
        update(loginEntity);

    }
    private void update(final LoginEntity loginEntity) {
        String token= SPreferenceUtil.getInstance(UpdatePwdActivity.this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"updateLoading");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(loginEntity));
        RequestManager.getInstance()
                .mServiceStore
                .updatePassword(token,body)
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
                                    //修改成功
                                    Toast.makeText(UpdatePwdActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(UpdatePwdActivity.this, LoginActivity.class);
                                    intent.putExtra("mobile",loginEntity.getMobile());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    finish();

                                }else {
                                    Toast.makeText(UpdatePwdActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(UpdatePwdActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        loadingDialogFragment.dismissAllowingStateLoss();
                        Log.e("oftenLineDel", "onError==" + msg);
                        Toast.makeText(UpdatePwdActivity.this, "修改失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
