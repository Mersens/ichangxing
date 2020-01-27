package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.MsgNoticeEntity;
import com.cxwl.ichangxing.entity.TXEntity;
import com.cxwl.ichangxing.utils.DateUtils;

import java.util.List;


public class TXAdapter extends RecyclerView.Adapter<TXAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<TXEntity> list;

    public TXAdapter(Context mContext, List<TXEntity> list){
        this.mContext=mContext;
        this.list=list;
        mInflater=LayoutInflater.from(mContext);

    }

    public void setDatas(List<TXEntity> list){
        this.list=list;
        notifyItemRangeChanged(0,this.list.size());
    }

    @NonNull
    @Override
    public TXAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.layout_tx_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TXAdapter.ViewHolder holder, final int i) {
        final TXEntity entity=list.get(i);
        holder.mTextTime.setText(DateUtils.stampToDate(entity.getEventTime()+"","yyyy-MM-dd HH:mm:ss"));
        holder.mTextMoney.setText(entity.getAmount()+"元");
        holder.mTextSXF.setText(entity.getCommission()+"元");
        int status=entity.getStatus();
        if(status==0){
            holder.mTextStatus.setText("申请中");
        }else if(status==1){
            holder.mTextStatus.setText("申请通过");
        }else {
            holder.mTextStatus.setText("未通过");
        }

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextTime;
        private TextView mTextMoney;
        private TextView mTextSXF;
        private TextView mTextStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTime=itemView.findViewById(R.id.tv_time);
            mTextMoney=itemView.findViewById(R.id.tv_money);
            mTextSXF=itemView.findViewById(R.id.tv_sxf);
            mTextStatus=itemView.findViewById(R.id.tv_status);

        }
    }


}
