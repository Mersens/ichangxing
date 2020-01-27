package com.cxwl.ichangxing.entity;

public class TrackLoadEntity {

    /**
     * afterLoadCommodityImg : string
     * afterLoadFormImg : string
     * afterLoadTruckBodyImg : string
     * beforeLoadCommodityImg : string
     * enterWeight : 0
     * id : string
     * latitude : 0
     * loadAddr : string
     * loadTime : 2020-01-14T12:08:11.050Z
     * longitude : 0
     * trackId : string
     * type : 0
     */

    private String afterLoadCommodityImg;
    private String afterLoadFormImg;
    private String afterLoadTruckBodyImg;
    private String beforeLoadCommodityImg;
    private int enterWeight;
    private String id;
    private double latitude;
    private String loadAddr;
    private String loadTime;
    private double longitude;
    private String trackId;
    private int type;

    public String getAfterLoadCommodityImg() {
        return afterLoadCommodityImg;
    }

    public void setAfterLoadCommodityImg(String afterLoadCommodityImg) {
        this.afterLoadCommodityImg = afterLoadCommodityImg;
    }

    public String getAfterLoadFormImg() {
        return afterLoadFormImg;
    }

    public void setAfterLoadFormImg(String afterLoadFormImg) {
        this.afterLoadFormImg = afterLoadFormImg;
    }

    public String getAfterLoadTruckBodyImg() {
        return afterLoadTruckBodyImg;
    }

    public void setAfterLoadTruckBodyImg(String afterLoadTruckBodyImg) {
        this.afterLoadTruckBodyImg = afterLoadTruckBodyImg;
    }

    public String getBeforeLoadCommodityImg() {
        return beforeLoadCommodityImg;
    }

    public void setBeforeLoadCommodityImg(String beforeLoadCommodityImg) {
        this.beforeLoadCommodityImg = beforeLoadCommodityImg;
    }

    public int getEnterWeight() {
        return enterWeight;
    }

    public void setEnterWeight(int enterWeight) {
        this.enterWeight = enterWeight;
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

    public String getLoadAddr() {
        return loadAddr;
    }

    public void setLoadAddr(String loadAddr) {
        this.loadAddr = loadAddr;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
