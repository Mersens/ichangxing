package com.cxwl.ichangxing.entity;

public class OrdLocationDto {
    private String endCountrySubdivisionCode;
    private String latitude;
    private String longitude;
    private String serialNumber;
    private String shippingNoteNumber;
    private String startCountrySubdivisionCode;

    @Override
    public String toString() {
        return "OrdLocationDto{" +
                "endCountrySubdivisionCode='" + endCountrySubdivisionCode + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", shippingNoteNumber='" + shippingNoteNumber + '\'' +
                ", startCountrySubdivisionCode='" + startCountrySubdivisionCode + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    private String type;//0=开始，1=结束，2=中间时段上传

    public String getEndCountrySubdivisionCode() {
        return endCountrySubdivisionCode;
    }

    public void setEndCountrySubdivisionCode(String endCountrySubdivisionCode) {
        this.endCountrySubdivisionCode = endCountrySubdivisionCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getShippingNoteNumber() {
        return shippingNoteNumber;
    }

    public void setShippingNoteNumber(String shippingNoteNumber) {
        this.shippingNoteNumber = shippingNoteNumber;
    }

    public String getStartCountrySubdivisionCode() {
        return startCountrySubdivisionCode;
    }

    public void setStartCountrySubdivisionCode(String startCountrySubdivisionCode) {
        this.startCountrySubdivisionCode = startCountrySubdivisionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
