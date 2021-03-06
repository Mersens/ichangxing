package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.GrabParamsEntity;
import com.cxwl.ichangxing.utils.DateUtils;

import java.util.List;


public class WaybillAdapter extends RecyclerView.Adapter<WaybillAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AssignParamsEntity> list;
    OnButtonClickListener listener;

    public WaybillAdapter(Context mContext, List<AssignParamsEntity> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setDatas(List<AssignParamsEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public WaybillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_waybill_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WaybillAdapter.ViewHolder holder, final int i) {
        final AssignParamsEntity entity = list.get(i);
        holder.mLayoutAddr.removeAllViews();
        List<AssignParamsEntity.LoadAddrBosBean> loadAddrs=entity.getLoadAddrBos();
        if(loadAddrs!=null){
            if(loadAddrs.size()>0){
                for (int j=0;j<loadAddrs.size();j++){
                    AssignParamsEntity.LoadAddrBosBean bosBean=loadAddrs.get(j);
                    holder.mLayoutAddr.addView(getAddrView("装货地址"+j,bosBean.getFullAddr()));
                }
            }
        }
        List<AssignParamsEntity.ReceiveAddrBosBean> receiveAddr=entity.getReceiveAddrBos();
        if(receiveAddr!=null){
            if(receiveAddr.size()>0){
                for (int K=0;K<receiveAddr.size();K++){
                    AssignParamsEntity.ReceiveAddrBosBean receiveBean=receiveAddr.get(K);
                    holder.mLayoutAddr.addView(getAddrView("卸货地址"+K,receiveBean.getFullAddr()));
                }
            }

        }
        holder.mTextHWMC.setText(entity.getCommodityName());
        holder.mTextDDBH.setText(entity.getOrderNo());
        holder.mTextDJ.setText(entity.getUnitprice()+"元/吨");
        holder.mTextZZL.setText(entity.getTotalWeight()+"吨");
        holder.mTextXDSJ.setText(entity.getOrderDateStr());
        holder.mTextZHSJ.setText(entity.getLoadDate());
        holder.mTextBZ.setText(entity.getRemark());
        int isGrap=entity.getIsGrab();
        if(isGrap==0){
            holder.mTextQX.setText("拒绝");
        }else {
            holder.mTextQX.setText("取消");
        }
        holder.mTextQX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onCancelClick(entity);
                }

            }
        });
        holder.mTextJD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onAcceptClick(entity);
                }
            }
        });
    }


    private View getAddrView(String key,String value){
        View view=mInflater.inflate(R.layout.layout_addr_view,null);
        TextView name=view.findViewById(R.id.name);
        name.setText(key);
        TextView vName=view.findViewById(R.id.tv_name);
        vName.setText(value);
        return view;
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextHWMC;
        private TextView mTextDDBH;
        private TextView mTextDJ;
        private TextView mTextZZL;
        private TextView mTextXDSJ;
        private TextView mTextZHSJ;
        private LinearLayout mLayoutAddr;
        private TextView mTextBZ;
        private TextView mTextQX;
        private TextView mTextJD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextHWMC = itemView.findViewById(R.id.tv_hwmc);
            mTextDDBH = itemView.findViewById(R.id.tv_ddbh);
            mTextDJ = itemView.findViewById(R.id.tv_dj);
            mTextZZL = itemView.findViewById(R.id.tv_zzl);
            mTextXDSJ = itemView.findViewById(R.id.tv_xdss);
            mTextZHSJ = itemView.findViewById(R.id.tv_zhsj);
            mLayoutAddr = itemView.findViewById(R.id.layout_address);
            mTextBZ = itemView.findViewById(R.id.tv_hpbz);
            mTextQX = itemView.findViewById(R.id.tv_jj);
            mTextJD = itemView.findViewById(R.id.tv_jd);
        }
    }

    public void  setOnButtonClickListener(OnButtonClickListener listener){
        this.listener=listener;

    }

    public interface OnButtonClickListener{
        void onCancelClick(AssignParamsEntity entity);
        void onAcceptClick(AssignParamsEntity entity);
    }

}
