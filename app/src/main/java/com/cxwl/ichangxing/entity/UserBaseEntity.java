package com.cxwl.ichangxing.entity;

public class UserBaseEntity {

    /**
     * realName : string
     * driverLicense : string
     * idCard : string
     * licenseImg : string
     * idCardFrontImg : string
     * idCardBackImg : string
     * personAndIdCardImg : string
     * certificate :
     * qualificationCertificate : string
     * qualificationCertificateImg : string
     * truckBo : {"plateNo":"string","licenseImg":"string","headImg":"string","insuranceImg":"string","operationImg":"string","insuranceStart":"","insuranceEnd":"","truckType":"string","length":"string","truckTypeName":"无","truckLengthName":"无","certificate":"","company":"string","mobile":"string","loadWeight":0,"transportImg":"string","transportNum":"string","hopperImg":"string","carCompanyId":"","carCompanyName":"","qualificationCertificate":"string","qualificationCertificateImg":"string"}
     * bankAccount : 123123123
     * balance : 1.0
     * withdraw : 1.0
     * driverCompanyId :
     * driverCompanyName :
     * carrier : string
     * roadTransportLicenseNo : string
     * carrierContact : string
     * carrierMobile : string
     * carrierRegisterTime :
     */

    private String realName;
    private String driverLicense;
    private String idCard;
    private String licenseImg;
    private String idCardFrontImg;
    private String idCardBackImg;
    private String personAndIdCardImg;
    private String certificate;
    private String qualificationCertificate;
    private String qualificationCertificateImg;
    private TruckBoBean truckBo;
    private String bankAccount;
    private double balance;
    private double withdraw;
    private String driverCompanyId;
    private String driverCompanyName;
    private String carrier;
    private String roadTransportLicenseNo;
    private String carrierContact;
    private String carrierMobile;
    private String carrierRegisterTime;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    public String getIdCardFrontImg() {
        return idCardFrontImg;
    }

    public void setIdCardFrontImg(String idCardFrontImg) {
        this.idCardFrontImg = idCardFrontImg;
    }

    public String getIdCardBackImg() {
        return idCardBackImg;
    }

    public void setIdCardBackImg(String idCardBackImg) {
        this.idCardBackImg = idCardBackImg;
    }

    public String getPersonAndIdCardImg() {
        return personAndIdCardImg;
    }

    public void setPersonAndIdCardImg(String personAndIdCardImg) {
        this.personAndIdCardImg = personAndIdCardImg;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getQualificationCertificate() {
        return qualificationCertificate;
    }

    public void setQualificationCertificate(String qualificationCertificate) {
        this.qualificationCertificate = qualificationCertificate;
    }

    public String getQualificationCertificateImg() {
        return qualificationCertificateImg;
    }

    public void setQualificationCertificateImg(String qualificationCertificateImg) {
        this.qualificationCertificateImg = qualificationCertificateImg;
    }

    public TruckBoBean getTruckBo() {
        return truckBo;
    }

    public void setTruckBo(TruckBoBean truckBo) {
        this.truckBo = truckBo;
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

    public String getDriverCompanyId() {
        return driverCompanyId;
    }

    public void setDriverCompanyId(String driverCompanyId) {
        this.driverCompanyId = driverCompanyId;
    }

    public String getDriverCompanyName() {
        return driverCompanyName;
    }

    public void setDriverCompanyName(String driverCompanyName) {
        this.driverCompanyName = driverCompanyName;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getRoadTransportLicenseNo() {
        return roadTransportLicenseNo;
    }

    public void setRoadTransportLicenseNo(String roadTransportLicenseNo) {
        this.roadTransportLicenseNo = roadTransportLicenseNo;
    }

    public String getCarrierContact() {
        return carrierContact;
    }

    public void setCarrierContact(String carrierContact) {
        this.carrierContact = carrierContact;
    }

    public String getCarrierMobile() {
        return carrierMobile;
    }

    public void setCarrierMobile(String carrierMobile) {
        this.carrierMobile = carrierMobile;
    }

    public String getCarrierRegisterTime() {
        return carrierRegisterTime;
    }

    public void setCarrierRegisterTime(String carrierRegisterTime) {
        this.carrierRegisterTime = carrierRegisterTime;
    }

    public static class TruckBoBean {
        /**
         * plateNo : string
         * licenseImg : string
         * headImg : string
         * insuranceImg : string
         * operationImg : string
         * insuranceStart :
         * insuranceEnd :
         * truckType : string
         * length : string
         * truckTypeName : 无
         * truckLengthName : 无
         * certificate :
         * company : string
         * mobile : string
         * loadWeight : 0.0
         * transportImg : string
         * transportNum : string
         * hopperImg : string
         * carCompanyId :
         * carCompanyName :
         * qualificationCertificate : string
         * qualificationCertificateImg : string
         */

        private String plateNo;
        private String licenseImg;
        private String headImg;
        private String insuranceImg;
        private String operationImg;
        private String insuranceStart;
        private String insuranceEnd;
        private String truckType;
        private String length;
        private String truckTypeName;
        private String truckLengthName;
        private String certificate;
        private String company;
        private String mobile;
        private double loadWeight;
        private String transportImg;
        private String transportNum;
        private String hopperImg;
        private String carCompanyId;
        private String carCompanyName;
        private String qualificationCertificate;
        private String qualificationCertificateImg;

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public String getLicenseImg() {
            return licenseImg;
        }

        public void setLicenseImg(String licenseImg) {
            this.licenseImg = licenseImg;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getInsuranceImg() {
            return insuranceImg;
        }

        public void setInsuranceImg(String insuranceImg) {
            this.insuranceImg = insuranceImg;
        }

        public String getOperationImg() {
            return operationImg;
        }

        public void setOperationImg(String operationImg) {
            this.operationImg = operationImg;
        }

        public String getInsuranceStart() {
            return insuranceStart;
        }

        public void setInsuranceStart(String insuranceStart) {
            this.insuranceStart = insuranceStart;
        }

        public String getInsuranceEnd() {
            return insuranceEnd;
        }

        public void setInsuranceEnd(String insuranceEnd) {
            this.insuranceEnd = insuranceEnd;
        }

        public String getTruckType() {
            return truckType;
        }

        public void setTruckType(String truckType) {
            this.truckType = truckType;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getTruckTypeName() {
            return truckTypeName;
        }

        public void setTruckTypeName(String truckTypeName) {
            this.truckTypeName = truckTypeName;
        }

        public String getTruckLengthName() {
            return truckLengthName;
        }

        public void setTruckLengthName(String truckLengthName) {
            this.truckLengthName = truckLengthName;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getLoadWeight() {
            return loadWeight;
        }

        public void setLoadWeight(double loadWeight) {
            this.loadWeight = loadWeight;
        }

        public String getTransportImg() {
            return transportImg;
        }

        public void setTransportImg(String transportImg) {
            this.transportImg = transportImg;
        }

        public String getTransportNum() {
            return transportNum;
        }

        public void setTransportNum(String transportNum) {
            this.transportNum = transportNum;
        }

        public String getHopperImg() {
            return hopperImg;
        }

        public void setHopperImg(String hopperImg) {
            this.hopperImg = hopperImg;
        }

        public String getCarCompanyId() {
            return carCompanyId;
        }

        public void setCarCompanyId(String carCompanyId) {
            this.carCompanyId = carCompanyId;
        }

        public String getCarCompanyName() {
            return carCompanyName;
        }

        public void setCarCompanyName(String carCompanyName) {
            this.carCompanyName = carCompanyName;
        }

        public String getQualificationCertificate() {
            return qualificationCertificate;
        }

        public void setQualificationCertificate(String qualificationCertificate) {
            this.qualificationCertificate = qualificationCertificate;
        }

        public String getQualificationCertificateImg() {
            return qualificationCertificateImg;
        }

        public void setQualificationCertificateImg(String qualificationCertificateImg) {
            this.qualificationCertificateImg = qualificationCertificateImg;
        }
    }
}
