package com.cxwl.ichangxing.entity;

import java.util.List;

public class OrderBoEntity {

    /**
     * acceptId : string
     * acceptStatus : 0
     * billStatus : 0
     * commodityName : string
     * contSize : 0
     * contType : string
     * containerYard : string
     * content : string
     * driverId : string
     * enterPort : string
     * expressImg : string
     * expressName : string
     * expressNo : string
     * grade : 0
     * isGrab : 0
     * loadAddrBos : [{"city":"string","contractPerson":"string","detail":"string","district":"string","fullAddr":"string","id":"string","latitude":0,"loadTime":"2020-01-15T01:13:22.665Z","longitude":0,"mobile":"string","province":"string","street":"string"}]
     * loadDate : string
     * orderNo : string
     * orderSource : 0
     * palletNo : string
     * prepayStatus : 0
     * receiptAddress : string
     * receiptStatus : 0
     * receiveAddrBos : [{"city":"string","commodityDetailBos":[{"description":"string","num":0,"standardId":"string"}],"contractPerson":"string","detail":"string","district":"string","fullAddr":"string","id":"string","latitude":0,"longitude":0,"mobile":"string","province":"string","street":"string"}]
     * receiveDate : 2020-01-15T01:13:22.665Z
     * remark : string
     * sendTime : 2020-01-15T01:13:22.665Z
     * sendType : 0
     * status : 0
     * totalOut : 0
     * totalOutStatus : 0
     * totalWeight : 0
     * type : 0
     * unitprice : 0
     */

    private String acceptId;
    private int acceptStatus;
    private int billStatus;
    private String commodityName;
    private int contSize;
    private String contType;
    private String containerYard;
    private String content;
    private String driverId;
    private String enterPort;
    private String expressImg;
    private String expressName;
    private String expressNo;
    private int grade;
    private int isGrab;
    private String loadDate;
    private String orderNo;
    private int orderSource;
    private String palletNo;
    private int prepayStatus;
    private String receiptAddress;
    private int receiptStatus;
    private String receiveDate;
    private String remark;
    private String sendTime;
    private int sendType;
    private int status;
    private int totalOut;
    private int totalOutStatus;
    private int totalWeight;
    private int type;
    private int unitprice;
    private List<LoadAddrBosBean> loadAddrBos;
    private List<ReceiveAddrBosBean> receiveAddrBos;

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

    public int getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(int billStatus) {
        this.billStatus = billStatus;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getExpressImg() {
        return expressImg;
    }

    public void setExpressImg(String expressImg) {
        this.expressImg = expressImg;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    public int getPrepayStatus() {
        return prepayStatus;
    }

    public void setPrepayStatus(int prepayStatus) {
        this.prepayStatus = prepayStatus;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(int totalOut) {
        this.totalOut = totalOut;
    }

    public int getTotalOutStatus() {
        return totalOutStatus;
    }

    public void setTotalOutStatus(int totalOutStatus) {
        this.totalOutStatus = totalOutStatus;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
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

    public static class LoadAddrBosBean {
        /**
         * city : string
         * contractPerson : string
         * detail : string
         * district : string
         * fullAddr : string
         * id : string
         * latitude : 0
         * loadTime : 2020-01-15T01:13:22.665Z
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
    }

    public static class ReceiveAddrBosBean {
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
}
