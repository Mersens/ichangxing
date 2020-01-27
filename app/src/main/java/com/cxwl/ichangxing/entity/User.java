package com.cxwl.ichangxing.entity;

public class User {
    /**
     * id : 352fdf10421c496c8738cb5600eefeab
     * realName :
     * mobile : 15738800385
     * grabNum : 0
     * grabIncome : 0
     * plateNo :
     * token : 8aac24085f584b40a3aad8ad6e11edc6
     * verificationStatus : 0
     * driverType : 1
     * bankAccount :
     * balance : 0
     * withdraw : 0
     * loginName : 15738800385
     * companyName :
     * birthday :
     * email :
     * settlementInterval :
     * mesNum :
     * openid :
     * bank :
     */

    private String id;
    private String realName;
    private String mobile;
    private int grabNum;
    private int grabIncome;
    private String plateNo;
    private String token;
    private int verificationStatus;// 认证状态【0=未认证，1=认证中，2=认证通过，3=认证失败】
    private int driverType;
    private String bankAccount;
    private double balance;
    private double withdraw;
    private String loginName;
    private String companyName;
    private String birthday;
    private String email;
    private String settlementInterval;
    private String mesNum;
    private String openid;
    private String bank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getGrabNum() {
        return grabNum;
    }

    public void setGrabNum(int grabNum) {
        this.grabNum = grabNum;
    }

    public int getGrabIncome() {
        return grabIncome;
    }

    public void setGrabIncome(int grabIncome) {
        this.grabIncome = grabIncome;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(int verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(double withdraw) {
        this.withdraw = withdraw;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSettlementInterval() {
        return settlementInterval;
    }

    public void setSettlementInterval(String settlementInterval) {
        this.settlementInterval = settlementInterval;
    }

    public String getMesNum() {
        return mesNum;
    }

    public void setMesNum(String mesNum) {
        this.mesNum = mesNum;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }



}
