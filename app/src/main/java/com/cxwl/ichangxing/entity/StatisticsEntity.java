package com.cxwl.ichangxing.entity;

public class StatisticsEntity {

    /**
     * weekNum : 2
     * weekAmount : 0
     * monthNum : 2
     * monthAmount : 0
     * seasonNum : 2
     * seasonAmount : 0
     * yearNum : 2
     * yearAmount : 0
     */

    private int weekNum;
    private double weekAmount;
    private int monthNum;
    private double monthAmount;
    private int seasonNum;
    private double seasonAmount;
    private int yearNum;
    private double yearAmount;

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public double getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(double weekAmount) {
        this.weekAmount = weekAmount;
    }

    public int getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(int monthNum) {
        this.monthNum = monthNum;
    }

    public double getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(double monthAmount) {
        this.monthAmount = monthAmount;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }

    public double getSeasonAmount() {
        return seasonAmount;
    }

    public void setSeasonAmount(int seasonAmount) {
        this.seasonAmount = seasonAmount;
    }

    public int getYearNum() {
        return yearNum;
    }

    public void setYearNum(int yearNum) {
        this.yearNum = yearNum;
    }

    public double getYearAmount() {
        return yearAmount;
    }

    public void setYearAmount(double yearAmount) {
        this.yearAmount = yearAmount;
    }
}
