package com.ridesharing.Entity;

import java.util.Date;

/**
 * Created by wensheng on 2014/11/10.
 */
public class Wish {
    private int id;
    /**
     * post user's id
     */
    private int uid;
    /**
     * driver's id
     */
    private int did;
    /**
     * join users' ids
     */
    private String joinIds;
    private String fromName;
    private String fromAdrr;
    private String fromCity;
    private String fromZipCode;
    private double fromlat;
    private double fromlng;
    private String toName;
    private String toAddr;
    private String toCity;
    private String toZipCode;
    private double tolat;
    private double tolng;
    private Date createTime;
    private Date startTime;
    private double price;
    private int status;

    public Wish(int uid, String fromAdrr, String fromCity, String fromZipCode, double fromlat, double fromlng, String toAddr, String toCity, String toZipCode, double tolat, double tolng, Date startTime, double price, int status) {
        this.uid = uid;
        this.fromAdrr = fromAdrr;
        this.fromCity = fromCity;
        this.fromZipCode = fromZipCode;
        this.fromlat = fromlat;
        this.fromlng = fromlng;
        this.toAddr = toAddr;
        this.toCity = toCity;
        this.toZipCode = toZipCode;
        this.tolat = tolat;
        this.tolng = tolng;
        this.startTime = startTime;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getJoinIds() {
        return joinIds;
    }

    public void setJoinIds(String joinIds) {
        this.joinIds = joinIds;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAdrr() {
        return fromAdrr;
    }

    public void setFromAdrr(String fromAdrr) {
        this.fromAdrr = fromAdrr;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getFromZipCode() {
        return fromZipCode;
    }

    public void setFromZipCode(String fromZipCode) {
        this.fromZipCode = fromZipCode;
    }

    public double getFromlat() {
        return fromlat;
    }

    public void setFromlat(double fromlat) {
        this.fromlat = fromlat;
    }

    public double getFromlng() {
        return fromlng;
    }

    public void setFromlng(double fromlng) {
        this.fromlng = fromlng;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getToZipCode() {
        return toZipCode;
    }

    public void setToZipCode(String toZipCode) {
        this.toZipCode = toZipCode;
    }

    public double getTolat() {
        return tolat;
    }

    public void setTolat(double tolat) {
        this.tolat = tolat;
    }

    public double getTolng() {
        return tolng;
    }

    public void setTolng(double tolng) {
        this.tolng = tolng;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
