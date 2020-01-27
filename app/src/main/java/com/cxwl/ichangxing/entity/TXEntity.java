package com.cxwl.ichangxing.entity;

public class TXEntity {

    /**
     * id : 3
     * eventTime : 1579156877000
     * status : 0
     * amount : 3
     * rate : 0
     * commission : 0
     * type : 72
     * checker : 系统管理员
     * checkTime : 1579156892000
     */

    private String id;
    private long eventTime;
    private int status;
    private int amount;
    private int rate;
    private int commission;
    private int type;
    private String checker;
    private long checkTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }
}
