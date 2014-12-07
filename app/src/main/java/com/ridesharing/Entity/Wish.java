package com.ridesharing.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ridesharing.Json.CustomJsonDateDeserializer;
import com.ridesharing.Json.CustomJsonDateSerializer;

import java.io.IOException;
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
    private String joinids;
    private String fromName;
    private String fromAddr;
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
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date createTime;
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date startTime;
    private double price;
    private String imageURL;
    private WishType type;
    private StatusType status;
    private int numOfPassenger;

    public Wish() {}

    public Wish(int uid, String fromAddr, String fromCity, String fromZipCode, double fromlat, double fromlng, String toAddr, String toCity, String toZipCode, double tolat, double tolng, Date startTime, double price,WishType type, int numOfPassenger ,StatusType status) {
        this.uid = uid;
        this.fromAddr = fromAddr;
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
        this.type = type;
        this.status = status;
        this.numOfPassenger = numOfPassenger;
    }
/*
    @JsonCreator
    public static Wish Create(String jsonString) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Wish wish = null;
        wish = mapper.readValue(jsonString, Wish.class);
        return wish;
    }
*/
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

    public String getJoinids() {
        return joinids;
    }

    public void setJoinids(String joinids) {
        this.joinids = joinids;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
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

    public WishType getType() {
        return type;
    }

    public void setType(WishType type) {
        this.type = type;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public int getNumOfPassenger() {
        return numOfPassenger;
    }

    public void setNumOfPassenger(int numOfPassenger) {
        this.numOfPassenger = numOfPassenger;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
