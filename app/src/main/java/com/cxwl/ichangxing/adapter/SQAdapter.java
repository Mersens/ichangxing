package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.SQEntity;
import com.cxwl.ichangxing.entity.TXEntity;
import com.cxwl.ichangxing.utils.DateUtils;

import java.util.List;


public class SQAdapter extends RecyclerView.Adapter<SQAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<SQEntity> list;

    public SQAdapter(Context mContext, List<SQEntity> list){
        this.mContext=mContext;
        this.list=list;
        mInflater=LayoutInflater.from(mContext);

    }

    public void setDatas(List<SQEntity> list){
        this.list=list;
        notifyItemRangeChanged(0,this.list.size());
    }

    @NonNull
    @Override
    public SQAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.layout_sq_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SQAdapter.ViewHolder holder, final int i) {
        final SQEntity entity=list.get(i);
        holder.mTextDDBH.setText(entity.getOrderNo());
        holder.mTextMoney.setText(entity.getPayableAmount()+"元");
        holder.mTextZJE.setText(entity.getCheckedAmount()+"元");
        holder.mTextDZFJE.setText(entity.getUncheckedAmount()+"元");
        holder.mTextTime.setText(DateUtils.stampToDate(entity.getRecheckTime()+"","yyyy-MM-dd HH:mm:ss"));



    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextDDBH;
        private TextView mTextMoney;
        private TextView mTextZJE;
        private TextView mTextDZFJE;
        private TextView mTextTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextDDBH=itemView.findViewById(R.id.tv_ddbh);
            mTextMoney=itemView.findViewById(R.id.tv_money);
            mTextZJE=itemView.findViewById(R.id.tv_zje);
            mTextDZFJE=itemView.findViewById(R.id.tv_dzfje);
            mTextTime=itemView.findViewById(R.id.tv_time);

        }
    }


}
