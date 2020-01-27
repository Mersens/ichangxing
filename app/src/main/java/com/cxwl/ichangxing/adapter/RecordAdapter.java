package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.AssignParamsEntity;
import com.cxwl.ichangxing.entity.OrderBoEntity;

import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<OrderBoEntity> list;
    OnButtonClickListener listener;

    public RecordAdapter(Context mContext, List<OrderBoEntity> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setDatas(List<OrderBoEntity> list) {
        this.list = list;
        notifyItemRangeChanged(0, this.list.size());
    }


    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_record_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, final int i) {
        final OrderBoEntity entity=list.get(i);
        holder.mTextHWMC.setText(entity.getCommodityName());
        holder.mTextDDBH.setText(entity.getOrderNo());
        holder.mTextDJ.setText(entity.getUnitprice()+"元/吨");
        holder.mTextZZL.setText(entity.getTotalWeight()+"吨");
        holder.mTextFCSJ.setText(entity.getSendTime());
        holder.mTextZHSJ.setText(entity.getLoadDate());
        holder.mLayoutAddr.removeAllViews();
        List<OrderBoEntity.LoadAddrBosBean> loadAddrs=entity.getLoadAddrBos();
        if(loadAddrs!=null){
            if(loadAddrs.size()>0){
                for (int j=0;j<loadAddrs.size();j++){
                    OrderBoEntity.LoadAddrBosBean bosBean=loadAddrs.get(j);
                    holder.mLayoutAddr.addView(getAddrView("装货地址"+j,bosBean.getFullAddr()));
                }
            }
        }
        List<OrderBoEntity.ReceiveAddrBosBean> receiveAddr=entity.getReceiveAddrBos();
        if(receiveAddr!=null){
            if(receiveAddr.size()>0){
                for (int K=0;K<receiveAddr.size();K++){
                    OrderBoEntity.ReceiveAddrBosBean receiveBean=receiveAddr.get(K);
                    holder.mLayoutAddr.addView(getAddrView("卸货地址"+K,receiveBean.getFullAddr()));
                }
            }
        }

        holder.mTextKDGS.setText(entity.getExpressName());
        holder.mTextWLDH.setText(entity.getExpressNo());
        int totalOutStatus=entity.getTotalOutStatus();
        if(totalOutStatus==0){
            holder.mTextSKZZ.setText("未支付");
        }else if(totalOutStatus==1){
            holder.mTextSKZZ.setText("已支付");
        }else {
            holder.mTextSKZZ.setText("");
        }

        holder.mBtn.setOnClickListener(new View.OnClickListener() {
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
        private TextView mTextDDBH;
        private TextView mTextHWMC;
        private TextView mTextDJ;
        private TextView mTextZZL;
        private TextView mTextFCSJ;
        private TextView mTextZHSJ;
        private LinearLayout mLayoutAddr;
        private TextView mTextSKZZ;
        private TextView mTextKDGS;
        private TextView mTextWLDH;
        private Button mBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextHWMC = itemView.findViewById(R.id.tv_hwmc);
            mTextDDBH = itemView.findViewById(R.id.tv_ddbh);
            mTextDJ = itemView.findViewById(R.id.tv_dj);
            mTextZZL = itemView.findViewById(R.id.tv_zzl);
            mTextFCSJ = itemView.findViewById(R.id.tv_fcss);
            mTextZHSJ = itemView.findViewById(R.id.tv_zhsj);
            mLayoutAddr = itemView.findViewById(R.id.layout_address);
            mTextSKZZ = itemView.findViewById(R.id.tv_skzt);
            mTextKDGS = itemView.findViewById(R.id.tv_kdgs);
            mTextWLDH = itemView.findViewById(R.id.tv_wldh);
            mBtn = itemView.findViewById(R.id.btn);
        }
    }

    public void  setOnButtonClickListener(OnButtonClickListener listener){
        this.listener=listener;

    }

    public interface OnButtonClickListener{
        void onAcceptClick(OrderBoEntity entity);
    }

}
