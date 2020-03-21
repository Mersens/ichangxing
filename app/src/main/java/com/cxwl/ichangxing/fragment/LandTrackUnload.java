package com.cxwl.ichangxing.fragment;

import java.util.List;

public class LandTrackUnload {


    /**
     * id : 198e4de42d2f46329bf0dad6c783d4ed
     * trackId : 3c9c234c693545e1a30cd3559b3126dc
     * type :
     * receiptUploadTime :
     * img1 : /usr/userData/changxing/appOrd/20200206/8190358fbe3d44c4b6fd36db8af31980..jpg
     * img2 :
     * img3 :
     * longitude : 0.0
     * latitude : 0.0
     * outWeight : 1
     * enterWeight : 0
     * unloadAddrs : ["安徽省-合肥市-瑶海区-明光路街道测试地址"]
     */

    private String id;
    private String trackId;
    private String type;
    private String receiptUploadTime;
    private String img1;
    private String img2;
    private String img3;
    private double longitude;
    private double latitude;
    private int outWeight;
    private int enterWeight;
    private List<String> unloadAddrs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiptUploadTime() {
        return receiptUploadTime;
    }

    public void setReceiptUploadTime(String receiptUploadTime) {
        this.receiptUploadTime = receiptUploadTime;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getOutWeight() {
        return outWeight;
    }

    public void setOutWeight(int outWeight) {
        this.outWeight = outWeight;
    }

    public int getEnterWeight() {
        return enterWeight;
    }

    public void setEnterWeight(int enterWeight) {
        this.enterWeight = enterWeight;
    }

    public List<String> getUnloadAddrs() {
        return unloadAddrs;
    }

    public void setUnloadAddrs(List<String> unloadAddrs) {
        this.unloadAddrs = unloadAddrs;
    }
}
