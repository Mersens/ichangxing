package com.cxwl.ichangxing.entity;

public class OftenLineEntity {

    /**
     * id : string
     * podAreaId : string
     * podAreaName : string
     * polAreaId : string
     * polAreaName : string
     */

    private String id;
    private String podAreaId;
    private String podAreaName;
    private String polAreaId;
    private String polAreaName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPodAreaId() {
        return podAreaId;
    }

    public void setPodAreaId(String podAreaId) {
        this.podAreaId = podAreaId;
    }

    public String getPodAreaName() {
        return podAreaName;
    }

    public void setPodAreaName(String podAreaName) {
        this.podAreaName = podAreaName;
    }

    public String getPolAreaId() {
        return polAreaId;
    }

    public void setPolAreaId(String polAreaId) {
        this.polAreaId = polAreaId;
    }

    public String getPolAreaName() {
        return polAreaName;
    }

    public void setPolAreaName(String polAreaName) {
        this.polAreaName = polAreaName;
    }
}
