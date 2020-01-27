package com.cxwl.ichangxing.activity;

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
import com.cxwl.ichangxing.entity.User;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.cxwl.ichangxing.view.LoadingDialogFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddBankActivity extends BaseActivity {
    private ImageView mImgBack;
    private TextView mTextTitle;
    private EditText mEditBank;
    private EditText mEditBankAccount;
    private EditText mEditName;
    private Button mBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addbank);
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
        mTextTitle.setText("绑定银行卡");

        mEditBank = findViewById(R.id.editBank);
        mEditBankAccount = findViewById(R.id.editAccount);
        mEditName = findViewById(R.id.editName);
        mBtn = findViewById(R.id.btnSave);
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
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });
    }

    private void doSave() {
        String token= SPreferenceUtil.getInstance(this).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this, "用户信息为空，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        String bank=mEditBank.getText().toString().trim();
        String bankAccount=mEditBankAccount.getText().toString().trim();
        String bankName=mEditName.getText().toString().trim();
        if(TextUtils.isEmpty(bank)){
            Toast.makeText(this, "银行名称为空！", Toast.LENGTH_SHORT).show();
            return;
            
        }
        if(TextUtils.isEmpty(bankAccount)){
            Toast.makeText(this, "银行账号为空！", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(bankName)){
            Toast.makeText(this, "银行账户名为空！", Toast.LENGTH_SHORT).show();
            return;

        }
        final LoadingDialogFragment loadingDialogFragment=LoadingDialogFragment.getInstance();
        loadingDialogFragment.showF(getSupportFragmentManager(),"bindbank");
        RequestManager.getInstance()
                .mServiceStore
                .bindBank(token,bank,bankAccount,bankName)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.e("incomeSt", "incomeSt==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    Toast.makeText(AddBankActivity.this, "绑定成功！", Toast.LENGTH_SHORT).show();
                                    mBtn.setClickable(false);
                                    mBtn.setEnabled(false);
                                    mBtn.setBackgroundResource(R.drawable.btn_noclick_bg);
                                }else {
                                    Toast.makeText(AddBankActivity.this, object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(AddBankActivity.this, "绑定失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("getLoad", "onError==" + msg);
                        Toast.makeText(AddBankActivity.this, "绑定失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));

        


    }
}
