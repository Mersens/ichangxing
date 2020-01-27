package com.cxwl.ichangxing.entity;

public class SQEntity {

    /**
     * orderNo : LYDD2020011300002
     * payNo : YFZD2020011600001
     * payableAmount : 1
     * checkedAmount : 1
     * uncheckedAmount : 0
     * verificationStatus :
     * recheckTime : 1579157968000
     * feeName : 运输费
     */

    private String orderNo;
    private String payNo;
    private int payableAmount;
    private int checkedAmount;
    private int uncheckedAmount;
    private String verificationStatus;
    private long recheckTime;
    private String feeName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public int getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(int payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getCheckedAmount() {
        return checkedAmount;
    }

    public void setCheckedAmount(int checkedAmount) {
        this.checkedAmount = checkedAmount;
    }

    public int getUncheckedAmount() {
        return uncheckedAmount;
    }

    public void setUncheckedAmount(int uncheckedAmount) {
        this.uncheckedAmount = uncheckedAmount;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public long getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(long recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
}
