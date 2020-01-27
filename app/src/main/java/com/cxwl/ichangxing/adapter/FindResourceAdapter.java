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
import com.cxwl.ichangxing.entity.GrabParamsEntity;
import com.cxwl.ichangxing.entity.MsgNoticeEntity;
import com.cxwl.ichangxing.utils.DateUtils;

import java.util.List;


public class FindResourceAdapter extends RecyclerView.Adapter<FindResourceAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<GrabParamsEntity> list;
    OnButtonClickListener listener;
    public FindResourceAdapter(Context mContext, List<GrabParamsEntity> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setDatas(List<GrabParamsEntity> list) {
        this.list = list;
        notifyItemRangeChanged(0, this.list.size());
    }


    @NonNull
    @Override
    public FindResourceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_find_resource_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FindResourceAdapter.ViewHolder holder, final int i) {
        final GrabParamsEntity entity = list.get(i);
        holder.mTextHWMC.setText(entity.getCommodityName());
        holder.mTextFBSJ.setText(entity.getCreateDateStr());
        holder.mTextStart.setText(entity.getAddr());
        holder.mTextEnd.setText(entity.getDesAddr());
        holder.mTextZZL.setText(entity.getTotalWeight()+"吨");
        holder.mTextDJ.setText(entity.getUnitprice()+"元/吨");
        holder.mTextZHSJ.setText(DateUtils.stampToDate(entity.getLoadDate()+"","yyyy-MM-dd HH:mm:ss"));
        holder.mTextBZ.setText(entity.getRemark());
        holder.mTextSY.setText(entity.getSurplusNum()+"车");
        holder.mTextHWMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(entity);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextHWMC;
        private TextView mTextFBSJ;
        private TextView mTextStart;
        private TextView mTextEnd;
        private TextView mTextZZL;
        private TextView mTextDJ;
        private TextView mTextZHSJ;
        private TextView mTextBZ;
        private TextView mTextSY;
        private TextView mTextQD;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextHWMC = itemView.findViewById(R.id.tv_hwmc);
            mTextFBSJ = itemView.findViewById(R.id.tv_fbsj);
            mTextStart = itemView.findViewById(R.id.tv_start);
            mTextEnd = itemView.findViewById(R.id.tv_end);
            mTextZZL = itemView.findViewById(R.id.tv_zll);
            mTextDJ = itemView.findViewById(R.id.tv_dj);
            mTextZHSJ = itemView.findViewById(R.id.tv_zhsj);
            mTextBZ = itemView.findViewById(R.id.tv_bz);
            mTextSY = itemView.findViewById(R.id.tv_sy);
            mTextQD = itemView.findViewById(R.id.tv_qd);
        }
    }


    public void  setOnButtonClickListener(OnButtonClickListener listener){
        this.listener=listener;

    }

    public interface OnButtonClickListener{
        void onClick(GrabParamsEntity entity);
    }

}
