package com.cxwl.ichangxing.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.activity.ReceiptActivity;
import com.cxwl.ichangxing.entity.EventEntity;
import com.cxwl.ichangxing.entity.ExpressEntity;
import com.cxwl.ichangxing.entity.OrderBoEntity;
import com.cxwl.ichangxing.utils.RequestManager;
import com.cxwl.ichangxing.utils.ResultObserver;
import com.cxwl.ichangxing.utils.RxBus;
import com.cxwl.ichangxing.utils.SPreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2018/3/22.
 */

public class ExpressSelectFragment extends DialogFragment {
    private ImageView mImgClose;
    private ListView mListView;
    private MyAdapter adapter;
    private List<ExpressEntity> mList=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NoticeDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_express_select_view, container, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initDatas();

    }

    private void initDatas() {

        String token= SPreferenceUtil.getInstance(getActivity()).getToken();
        if(TextUtils.isEmpty(token)){
            Toast.makeText(getActivity(), "token为空，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }

        final LoadingDialogFragment dialogFragment=LoadingDialogFragment.getInstance();
        dialogFragment.showF(getChildFragmentManager(),"receiptView");
        RequestManager.getInstance()
                .mServiceStore
                .dict(token,"express")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver(new RequestManager.onRequestCallBack() {
                    @Override
                    public void onSuccess(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("receipt", "drive==" + msg);
                        if(!TextUtils.isEmpty(msg)){
                            try {
                                JSONObject object=new JSONObject(msg);
                                if(object.getInt("code")==0){
                                    //上传成功
                                    JSONArray array=object.getJSONArray("data");
                                    mList.clear();
                                    Gson gson=new Gson();
                                    for (int i = 0; i < array.length(); i++) {
                                        String str = array.getString(i);
                                        ExpressEntity entity=gson.fromJson(str,ExpressEntity.class);
                                        mList.add(entity);
                                        adapter.setDatas(mList);
                                    }

                                }else {
                                    Toast.makeText(getActivity(), object.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogFragment.dismissAllowingStateLoss();
                        Log.e("uploadCargo", "onError==" + msg);
                        Toast.makeText(getActivity(), "失败！"+msg, Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void initView(View view) {
        mImgClose = view.findViewById(R.id.img_close);
        mListView=view.findViewById(R.id.listView);
        adapter=new MyAdapter(getActivity(),mList);
        mListView.setAdapter(adapter);
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener!=null){
                    ExpressEntity expressEntity=mList.get(position);
                    listener.onSelect(expressEntity.getLabel());
                    dismissAllowingStateLoss();
                }
            }
        });

    }

    public static ExpressSelectFragment getInstance() {
        ExpressSelectFragment fragment = new ExpressSelectFragment();

        return fragment;

    }

    public void showF(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    private OnDialogClickListener listener;

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public interface OnDialogClickListener {
        void onSelect(String name);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private List<ExpressEntity> list;
        private LayoutInflater inflater;

        public MyAdapter(Context context, List<ExpressEntity> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        public void setDatas(List<ExpressEntity> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ExpressEntity expressEntity=mList.get(position);
           View view=inflater.inflate(R.layout.layout_express_item,null);
            TextView name=view.findViewById(R.id.name);
            name.setText(expressEntity.getLabel());
            return view;
        }
    }

}
