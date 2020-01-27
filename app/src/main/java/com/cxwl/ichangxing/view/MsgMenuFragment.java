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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxwl.ichangxing.R;


/**
 * Created by Administrator on 2018/3/22.
 */

public class MsgMenuFragment extends DialogFragment {
    private ImageView mImgClose;
    private RelativeLayout mLayoutYD;
    private RelativeLayout mLayoutHL;
    private int readStatus;
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
        return inflater.inflate(R.layout.layout_msg_menu,container,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mImgClose=view.findViewById(R.id.img_close);
        mLayoutYD=view.findViewById(R.id.layout_yd);
        mLayoutHL=view.findViewById(R.id.layout_hl);
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
        mLayoutYD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readStatus=1;
                if(listener!=null){
                    listener.onSelect(readStatus);
                }
            }
        });
        mLayoutHL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readStatus=2;
                if(listener!=null){
                    listener.onSelect(readStatus);
                }
            }
        });
    }

    public static MsgMenuFragment getInstance(){
        MsgMenuFragment fragment=new MsgMenuFragment();

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
        void onSelect(int readStatus);

    }

}
