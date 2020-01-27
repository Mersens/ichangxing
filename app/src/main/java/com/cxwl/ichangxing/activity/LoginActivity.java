package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.entity.LoginEntity;
import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.ResultEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.utils.SystemUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity {
    private EditText mEditMobile;
    private EditText mEditPwd;
    private TextView mTextRegister;
    private Button mBtnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();

    }

    private void initView() {
        mEditMobile = findViewById(R.id.editPhone);
        mEditPwd = findViewById(R.id.editPass);
        mTextRegister = findViewById(R.id.tv_register);
        mBtnLogin = findViewById(R.id.btnlogin);
        Intent intent=getIntent();
        if(intent.hasExtra("mobile")){
            String mobile=intent.getStringExtra("mobile");
            mEditMobile.setText(mobile);
        }

    }

    private void initEvent() {
        mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册
                doRegister();
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录
                doLogin();
            }
        });
    }

    private void doRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void doLogin() {
        final String mobile=mEditMobile.getText().toString();
        final String pwd=mEditPwd.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            Toast.makeText(this, "手机号为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mobile.length()!=11){
            Toast.makeText(this, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialogFragment dialogFragment=LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(),"loginView");
        LoginEntity login=new LoginEntity();
        login.setDeviceNumber(SystemUtil.getSystemModel());
        login.setMobile(mobile);
        login.setPassword(pwd);
        final Gson gson=new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(login));
        RequestManager.getInstance()
                .mServiceStore
                .login(body)
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
                                    //登录成功
                                    JSONObject result=object.getJSONObject("data");
                                    Log.e("result",result.toString());
                                    String token=result.getString("token");
                                    SPreferenceUtil.getInstance(LoginActivity.this).setUserId(mobile);
                                    SPreferenceUtil.getInstance(LoginActivity.this).setUserinfo(result.toString());
                                    SPreferenceUtil.getInstance(LoginActivity.this).setToken(token);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("login", "onError==" + msg);
                        Toast.makeText(LoginActivity.this, "登录失败！"+msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }


}
