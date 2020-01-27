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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;


/**
 * Created by Administrator on 2018/3/22.
 */

public class CancelDialogFragment extends DialogFragment {
    private ImageView mImgClose;
    private ContainsEmojiEditText mEdit;
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

        return inflater.inflate(R.layout.layout_cancel_order,container,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mImgClose=view.findViewById(R.id.img_close);
        mEdit=view.findViewById(R.id.editText);
        mBtn=view.findViewById(R.id.btn);
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String msg=mEdit.getText().toString().trim();
               if(TextUtils.isEmpty(msg)){
                   Toast.makeText(getActivity(), "请填写原因！", Toast.LENGTH_SHORT).show();
                   return;
               }
               if(listener!=null){
                   listener.onClickOk(msg);
               }
            }
        });

    }

    public static CancelDialogFragment getInstance(){
        CancelDialogFragment fragment=new CancelDialogFragment();
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
        void onClickOk(String msg);

    }

}
