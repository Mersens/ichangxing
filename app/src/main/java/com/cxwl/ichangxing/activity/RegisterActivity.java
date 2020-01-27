package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.cxwl.ichangxing.entity.ResultEntity;
import com.cxwl.ichangxing.utils.RegisterCodeTimer;
import com.cxwl.ichangxing.utils.RegisterCodeTimerService;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SystemUtil;
import com.cxwl.ichangxing.view.ExitDialogFragment;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;

public class RegisterActivity extends BaseActivity {

    private ImageView mImgBack;
    private TextView mTextTitle;
    private EditText mEditMobile;
    private EditText mEditPwd;
    private EditText mEditPwdAgain;
    private EditText mEditCode;
    private TextView mTextGetCode;
    private Button mBtnRegister;
    private String ckMobile;
    private String ckCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        init();
    }

    @Override
    public void init() {
        initView();
        initEvent();
        RegisterCodeTimerService.setHandler(mCodeHandler);
    }

    private void initView() {
        mEditMobile = findViewById(R.id.editPhone);
        mEditPwd = findViewById(R.id.editPass);
        mEditPwdAgain = findViewById(R.id.editPassAgin);
        mEditCode = findViewById(R.id.edCode);

        mTextGetCode = findViewById(R.id.tv_getCode);
        mBtnRegister = findViewById(R.id.btnreg);

        mImgBack = findViewById(R.id.imgBack);
        mTextTitle = findViewById(R.id.tv_title);
        mTextTitle.setText("注册");
    }

    private void initEvent() {
        mTextGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取验证码
                try {
                    doGetCode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册
                doRegister();

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

    private void doRegister() {
        final String mobile = mEditMobile.getText().toString();
        final String pwd = mEditPwd.getText().toString();
        final String pwdAgain = mEditPwdAgain.getText().toString();
        final String code = mEditCode.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "手机号为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() != 11) {
            Toast.makeText(this, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码手机号！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6) {
            Toast.makeText(this, "密码至少6位！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwdAgain)) {
            Toast.makeText(this, "确认密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwdAgain.equals(pwd)) {
            Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "验证码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!code.equals(ckCode)) {
            Toast.makeText(this, "验证码错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mobile.equals(ckMobile)){
            Toast.makeText(this, "手机号码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setDeviceNumber(SystemUtil.getSystemModel());
        loginEntity.setMobile(mobile);
        loginEntity.setPassword(pwd);
        loginEntity.setVerifyCode(code);
        register(loginEntity);

    }

    private void register(final LoginEntity loginEntity) {
        final LoadingDialogFragment dialogFragment = LoadingDialogFragment.getInstance();
        dialogFragment.showF(getSupportFragmentManager(), "registerView");
        final Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), gson.toJson(loginEntity));
        RequestManager.getInstance()
                .mServiceStore
                .register(body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("register", "register==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            ResultEntity result = gson.fromJson(msg, ResultEntity.class);
                            if (result.getCode() == 0) {
                                //注册成功
                                showRegisterView(loginEntity.getMobile());
                            } else {
                                Toast.makeText(RegisterActivity.this, result.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("login", "onError==" + msg);
                        Toast.makeText(RegisterActivity.this, "注册失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));


    }

    private void showRegisterView(final String mobile) {
        String msg="注册成功，到登录页面进行登录！";
       final ExitDialogFragment exitDialogFragment=ExitDialogFragment.getInstance(msg);
        exitDialogFragment.setOnDialogClickListener(new ExitDialogFragment.OnDialogClickListener() {
            @Override
            public void onClickCancel() {
                exitDialogFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onClickOk() {
                exitDialogFragment.dismissAllowingStateLoss();
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();


            }
        });
        exitDialogFragment.showF(getSupportFragmentManager(),"registerView");

    }

    private void doGetCode() throws JSONException {
        final String mobile = mEditMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "手机号为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() != 11) {
            Toast.makeText(this, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
            return;
        }
        startService(new Intent(RegisterActivity.this,
                RegisterCodeTimerService.class));
        RequestManager.getInstance()
                .mServiceStore
                .getMobileCode(mobile)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Gson gson = new Gson();
                        Log.e("getMobileCode", "getMobileCode==" + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            ResultEntity result = gson.fromJson(msg, ResultEntity.class);
                            if (result.getCode() == 0) {
                                //获取成功
                                ckCode = result.getData();
                                ckMobile = mobile;
                                Toast.makeText(RegisterActivity.this, "验证码已发送！",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, result.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "获取失败！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getMobileCode", "onError==" + msg);
                        Toast.makeText(RegisterActivity.this, "获取失败！" + msg, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    Handler mCodeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
                mTextGetCode.setText(msg.obj.toString());
                mTextGetCode.setEnabled(false);
            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
                mTextGetCode.setEnabled(true);
                mTextGetCode.setText("获取验证码");
            }
        }

        ;
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerService();
        mCodeHandler.removeCallbacksAndMessages(null);
    }


    public void stopTimerService() {
        stopService(new Intent(RegisterActivity.this,
                RegisterCodeTimerService.class));
    }
}
