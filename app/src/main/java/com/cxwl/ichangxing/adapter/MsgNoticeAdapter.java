package com.cxwl.ichangxing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.entity.MsgNoticeEntity;
import com.cxwl.ichangxing.entity.OftenLineEntity;
import com.cxwl.ichangxing.utils.DateUtils;

import java.util.List;


public class MsgNoticeAdapter extends RecyclerView.Adapter<MsgNoticeAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MsgNoticeEntity> list;
    private OnMsgClickListener lineDelListener;

    public MsgNoticeAdapter(Context mContext, List<MsgNoticeEntity> list){
        this.mContext=mContext;
        this.list=list;
        mInflater=LayoutInflater.from(mContext);

    }

    public void setDatas(List<MsgNoticeEntity> list){
        this.list=list;
        notifyItemRangeChanged(0,this.list.size());
    }


    @NonNull
    @Override
    public MsgNoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.layout_msg_notice_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MsgNoticeAdapter.ViewHolder holder, final int i) {
        final MsgNoticeEntity entity=list.get(i);
        holder.mTextTitle.setText(entity.getTitle());
        holder.mTextTime.setText(DateUtils.stampToDate(entity.getDate()+"","yyyy-MM-dd HH:mm:ss"));

        holder.mTextMsg.setText(entity.getMessage());
        int readStatus=entity.getReadStatus();
        if(readStatus==0){
            holder.mImgStatus.setImageResource(R.mipmap.img_wd);

        }else if(readStatus==1){
            holder.mImgStatus.setImageResource(R.mipmap.img_yd);
        }else if(readStatus==2){
            holder.mImgStatus.setImageResource(R.mipmap.img_hl);
        }
        holder.mImgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lineDelListener!=null){
                    lineDelListener.onDel(entity);
                }

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lineDelListener!=null){
                    lineDelListener.onClick(entity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextTitle;
        private TextView mTextMsg;
        private TextView mTextTime;
        private ImageView mImgStatus;
        private CardView cardView;
        private ImageView mImgDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            mTextTitle=itemView.findViewById(R.id.tv_title);
            mTextMsg=itemView.findViewById(R.id.tv_msg);
            mTextTime=itemView.findViewById(R.id.tv_time);
            mImgStatus=itemView.findViewById(R.id.img_status);
            mImgDel=itemView.findViewById(R.id.img_del);
        }
    }

    public void setOnMsgClickListener(OnMsgClickListener lineDelListener){
        this.lineDelListener=lineDelListener;
    }


    public interface OnMsgClickListener{
        void onClick(MsgNoticeEntity entity);
        void onDel(MsgNoticeEntity entity);
    }
}
