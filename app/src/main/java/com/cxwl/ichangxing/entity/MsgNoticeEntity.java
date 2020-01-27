package com.cxwl.ichangxing.entity;

public class MsgNoticeEntity {

    /**
     * noticeId : 01c5dcea90a94cdcba6626cae45acd20
     * message : 您有一个新的订单LYDD2020011300001,请及时处理!
     * publishName :
     * title : 订单生成
     * date : 1578882083000
     * readStatus : 1
     */

    private String noticeId;
    private String message;
    private String publishName;
    private String title;
    private long date;
    private int readStatus;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
