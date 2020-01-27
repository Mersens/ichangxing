package com.cxwl.ichangxing.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;


/**
 * Created by Administrator on 2018/3/22.
 */

public class TXViewFragment extends DialogFragment {
    private ImageView mImgClose;
    private double withdraw;
    private String account;
    private TextView mTextWithdraw;
    private TextView mTextAccount;
    private EditText mEditMoney;
    private Button mBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.NoticeDialogStyle);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_tx_view,container,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        withdraw=getArguments().getDouble("withdraw");
        account=getArguments().getString("account");
        initView(view);
    }

    private void initView(View view) {
        mImgClose=view.findViewById(R.id.img_close);
        mTextWithdraw=view.findViewById(R.id.tv_zgktx);
        mTextAccount=view.findViewById(R.id.tv_account);
        mEditMoney=view.findViewById(R.id.editMoney);
        mBtn=view.findViewById(R.id.btn);
        mTextWithdraw.setText(withdraw+"");
        mTextAccount.setText(account);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTx();
            }
        });
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
    }

    private void doTx() {
        final String strMoney=mEditMoney.getText().toString().trim();
        if(TextUtils.isEmpty(strMoney)){
            Toast.makeText(getActivity(), "请输入提现金额", Toast.LENGTH_SHORT).show();
            return;
        }
        double money=Double.parseDouble(strMoney);
        if(money>withdraw){
            Toast.makeText(getActivity(), "请输入正确提现金额！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(listener!=null){
            listener.onSelect(money);
        }
    }

    public static TXViewFragment getInstance(double withdraw,String account){
        TXViewFragment fragment=new TXViewFragment();
        Bundle bundle=new Bundle();
        bundle.putDouble("withdraw",withdraw);
        bundle.putString("account",account);
        fragment.setArguments(bundle);
        return fragment;

    }

    public void showF(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    private OnDialogClickListener listener;
    public void setOnDialogClickListener(OnDialogClickListener listener){
        this.listener=listener;
    }

    public interface OnDialogClickListener {
        void onSelect(double money);

    }

}
