package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.CXEntity;

import java.util.List;

public class CXSpinnerAdapter extends BaseAdapter {
    private List<CXEntity> mList;
    private Context context;
    private LayoutInflater inflater;
    public CXSpinnerAdapter(Context context,List<CXEntity> mList){
        this.context=context;
        this.mList=mList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CXEntity entity=mList.get(position);
        View view=inflater.inflate(R.layout.layout_spinner_item,null);
        TextView name=view.findViewById(R.id.name);
        name.setText(entity.getLabel());
        return view;
    }
}
