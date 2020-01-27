package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.DedicatedLineBoEntity;
import com.cxwl.ichangxing.entity.OftenLineEntity;

import java.util.List;


public class OftenLineListAdapter extends RecyclerView.Adapter<OftenLineListAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<DedicatedLineBoEntity> list;
    private OnOftenLineDelListener lineDelListener;

    public OftenLineListAdapter(Context mContext, List<DedicatedLineBoEntity> list){
        this.mContext=mContext;
        this.list=list;
        mInflater=LayoutInflater.from(mContext);

    }

    public void setDatas(List<DedicatedLineBoEntity> list){
        this.list=list;
        notifyItemRangeChanged(0,this.list.size());
    }


    @NonNull
    @Override
    public OftenLineListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.layout_oftenline_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OftenLineListAdapter.ViewHolder holder, final int i) {
        final DedicatedLineBoEntity entity=list.get(i);
            holder.mTextStart.setText(entity.getOrigin());

            holder.mTextEnd.setText(entity.getDestination());

        holder.mImgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lineDelListener!=null){
                    lineDelListener.onDel(entity);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextStart;
        private TextView mTextEnd;
        private ImageView mImgDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextStart=itemView.findViewById(R.id.tv_start);
            mTextEnd=itemView.findViewById(R.id.tv_end);
            mImgDel=itemView.findViewById(R.id.img_del);
        }
    }

    public void setOnOftenLineDelListener(OnOftenLineDelListener lineDelListener){

        this.lineDelListener=lineDelListener;
    }


    public interface OnOftenLineDelListener{
        void onDel(DedicatedLineBoEntity entity);
    }
}
