package com.cxwl.ichangxing.entity;

import java.io.Serializable;
import java.util.List;

public class AssignParamsEntity implements Serializable {


    /**
     * acceptId : string
     * acceptStatus : 0
     * banNo : string
     * billStatus : 0
     * boxNo : string
     * closingTime : 2020-01-11T04:25:08.301Z
     * commodityName : string
     * contSize : 0
     * contType : string
     * containerYard : string
     * createDate : 2020-01-11T04:25:08.301Z
     * driverId : string
     * enterPort : string
     * extractNo : string
     * isGrab : 0
     * loadAddrBos : [{"city":"string","contractPerson":"string","detail":"string","district":"string","fullAddr":"string","id":"string","latitude":0,"loadTime":"2020-01-11T04:25:08.301Z","longitude":0,"mobile":"string","province":"string","street":"string"}]
     * loadDate : string
     * orderDate : 2020-01-11T04:25:08.301Z
     * orderDateStr : string
     * orderNo : string
     * orderSource : 0
     * palletNo : string
     * receiptStatus : 0
     * receiveAddrBos : [{"city":"string","commodityDetailBos":[{"description":"string","num":0,"standardId":"string"}],"contractPerson":"string","detail":"string","district":"string","fullAddr":"string","id":"string","latitude":0,"longitude":0,"mobile":"string","province":"string","street":"string"}]
     * receiveDate : string
     * remark : string
     * returnYard : string
     * sendType : 0
     * shipName : string
     * status : 0
     * totalBulk : 0
     * totalOut : 0
     * totalWeight : 0
     * trackId : string
     * trackStepBos : [{"code":"string","name":"string","status":true}]
     * type : 0
     * unitprice : 0
     * voyageNo : string
     */

    private String acceptId;
    private int acceptStatus;
    private String banNo;
    private int billStatus;
    private String boxNo;
    private String closingTime;
    private String commodityName;
    private int contSize;
    private String contType;
    private String containerYard;
    private String createDate;
    private String driverId;
    private String enterPort;
    private String extractNo;
    private int isGrab;
    private String loadDate;
    private String orderDate;
    private String orderDateStr;
    private String orderNo;
    private int orderSource;
    private String palletNo;
    private int receiptStatus;
    private String receiveDate;
    private String remark;
    private String returnYard;
    private int sendType;
    private String shipName;
    private int status;
    private int totalBulk;
    private int totalOut;
    private int totalWeight;
    private String trackId;
    private int type;
    private int unitprice;
    private String voyageNo;
    private List<LoadAddrBosBean> loadAddrBos;
    private List<ReceiveAddrBosBean> receiveAddrBos;
    private List<TrackStepBosBean> trackStepBos;

    public String getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public int getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(int acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public String getBanNo() {
        return banNo;
    }

    public void setBanNo(String banNo) {
        this.banNo = banNo;
    }

    public int getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(int billStatus) {
        this.billStatus = billStatus;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getContSize() {
        return contSize;
    }

    public void setContSize(int contSize) {
        this.contSize = contSize;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getContainerYard() {
        return containerYard;
    }

    public void setContainerYard(String containerYard) {
        this.containerYard = containerYard;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getEnterPort() {
        return enterPort;
    }

    public void setEnterPort(String enterPort) {
        this.enterPort = enterPort;
    }

    public String getExtractNo() {
        return extractNo;
    }

    public void setExtractNo(String extractNo) {
        this.extractNo = extractNo;
    }

    public int getIsGrab() {
        return isGrab;
    }

    public void setIsGrab(int isGrab) {
        this.isGrab = isGrab;
    }

    public String getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDateStr() {
        return orderDateStr;
    }

    public void setOrderDateStr(String orderDateStr) {
        this.orderDateStr = orderDateStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(int orderSource) {
        this.orderSource = orderSource;
    }

    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public int getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(int receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnYard() {
        return returnYard;
    }

    public void setReturnYard(String returnYard) {
        this.returnYard = returnYard;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalBulk() {
        return totalBulk;
    }

    public void setTotalBulk(int totalBulk) {
        this.totalBulk = totalBulk;
    }

    public int getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(int totalOut) {
        this.totalOut = totalOut;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
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

    public int getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public List<LoadAddrBosBean> getLoadAddrBos() {
        return loadAddrBos;
    }

    public void setLoadAddrBos(List<LoadAddrBosBean> loadAddrBos) {
        this.loadAddrBos = loadAddrBos;
    }

    public List<ReceiveAddrBosBean> getReceiveAddrBos() {
        return receiveAddrBos;
    }

    public void setReceiveAddrBos(List<ReceiveAddrBosBean> receiveAddrBos) {
        this.receiveAddrBos = receiveAddrBos;
    }

    public List<TrackStepBosBean> getTrackStepBos() {
        return trackStepBos;
    }

    public void setTrackStepBos(List<TrackStepBosBean> trackStepBos) {
        this.trackStepBos = trackStepBos;
    }

    public static class LoadAddrBosBean implements Serializable {
        /**
         * city : string
         * contractPerson : string
         * detail : string
         * district : string
         * fullAddr : string
         * id : string
         * latitude : 0
         * loadTime : 2020-01-11T04:25:08.301Z
         * longitude : 0
         * mobile : string
         * province : string
         * street : string
         */

        private String city;
        private String contractPerson;
        private String detail;
        private String district;
        private String fullAddr;
        private String id;
        private double latitude;
        private String loadTime;
        private double longitude;
        private String mobile;
        private String province;
        private String street;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getContractPerson() {
            return contractPerson;
        }

        public void setContractPerson(String contractPerson) {
            this.contractPerson = contractPerson;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getFullAddr() {
            return fullAddr;
        }

        public void setFullAddr(String fullAddr) {
            this.fullAddr = fullAddr;
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

        public String getLoadTime() {
            return loadTime;
        }

        public void setLoadTime(String loadTime) {
            this.loadTime = loadTime;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    public static class ReceiveAddrBosBean implements Serializable{
        /**
         * city : string
         * commodityDetailBos : [{"description":"string","num":0,"standardId":"string"}]
         * contractPerson : string
         * detail : string
         * district : string
         * fullAddr : string
         * id : string
         * latitude : 0
         * longitude : 0
         * mobile : string
         * province : string
         * street : string
         */

        private String city;
        private String contractPerson;
        private String detail;
        private String district;
        private String fullAddr;
        private String id;
        private double latitude;
        private double longitude;
        private String mobile;
        private String province;
        private String street;
        private List<CommodityDetailBosBean> commodityDetailBos;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getContractPerson() {
            return contractPerson;
        }

        public void setContractPerson(String contractPerson) {
            this.contractPerson = contractPerson;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getFullAddr() {
            return fullAddr;
        }

        public void setFullAddr(String fullAddr) {
            this.fullAddr = fullAddr;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public List<CommodityDetailBosBean> getCommodityDetailBos() {
            return commodityDetailBos;
        }

        public void setCommodityDetailBos(List<CommodityDetailBosBean> commodityDetailBos) {
            this.commodityDetailBos = commodityDetailBos;
        }

        public static class CommodityDetailBosBean {
            /**
             * description : string
             * num : 0
             * standardId : string
             */

            private String description;
            private int num;
            private String standardId;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getStandardId() {
                return standardId;
            }

            public void setStandardId(String standardId) {
                this.standardId = standardId;
            }
        }
    }

    public static class TrackStepBosBean implements Serializable{
        /**
         * code : string
         * name : string
         * status : true
         */

        private String code;
        private String name;
        private boolean status;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
