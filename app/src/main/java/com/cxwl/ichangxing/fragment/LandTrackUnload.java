package com.cxwl.ichangxing.fragment;

public class LandTrackUnload {
    /**
     * afterCommodityImg : string
     * afterTruckBodyImg : string
     * beforeCommodity : string
     * beforeTruckBodyImg : string
     * id : string
     * latitude : 0
     * longitude : 0
     * outWeight : 0
     * trackId : string
     * unloadAddr : string
     * unloadTime : 2020-01-14T12:08:11.050Z
     */

    private String afterCommodityImg;
    private String afterTruckBodyImg;
    private String beforeCommodity;
    private String beforeTruckBodyImg;
    private String id;
    private double latitude;
    private double longitude;
    private int outWeight;
    private String trackId;
    private String unloadAddr;
    private String unloadTime;

    public String getAfterCommodityImg() {
        return afterCommodityImg;
    }

    public void setAfterCommodityImg(String afterCommodityImg) {
        this.afterCommodityImg = afterCommodityImg;
    }

    public String getAfterTruckBodyImg() {
        return afterTruckBodyImg;
    }

    public void setAfterTruckBodyImg(String afterTruckBodyImg) {
        this.afterTruckBodyImg = afterTruckBodyImg;
    }

    public String getBeforeCommodity() {
        return beforeCommodity;
    }

    public void setBeforeCommodity(String beforeCommodity) {
        this.beforeCommodity = beforeCommodity;
    }

    public String getBeforeTruckBodyImg() {
        return beforeTruckBodyImg;
    }

    public void setBeforeTruckBodyImg(String beforeTruckBodyImg) {
        this.beforeTruckBodyImg = beforeTruckBodyImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOutWeight() {
        return outWeight;
    }

    public void setOutWeight(int outWeight) {
        this.outWeight = outWeight;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUnloadAddr() {
        return unloadAddr;
    }

    public void setUnloadAddr(String unloadAddr) {
        this.unloadAddr = unloadAddr;
    }

    public String getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(String unloadTime) {
        this.unloadTime = unloadTime;
    }
}
