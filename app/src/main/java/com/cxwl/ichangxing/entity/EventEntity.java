package com.cxwl.ichangxing.entity;

/**
 * Created by Administrator on 2018/3/21.
 */

public class EventEntity {
    public static final int NOTICE_MSG=10001;
    public static final int START_CAR=10002;
    public static final int START_CAR_REFRESH=10003;
    public static final int RECORD_REFRESH=10004;
    public static final int USER_INFO_REFRESH=10005;
    public static final int WAYBILL_INFO_REFRESH=10006;
    public static final int FINISH=10007;
    public int type;
    public String value;
    public EventEntity(){
    }

    public EventEntity(int type){
        this.type=type;
    }


    public EventEntity(int type, String value){
        this.type=type;
        this.value=value;
    }



}
