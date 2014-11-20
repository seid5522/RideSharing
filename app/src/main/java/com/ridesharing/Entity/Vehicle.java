/*
 * Copyright (C) 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ridesharing.Entity;

/**
 * @Package com.ridesharing.Entity
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class Vehicle {
    private int id;
    /**
     * driver's id number
     */
    private int did;
    private String maker;
    private String model;
    private int year;
    /**
     * number of passengers;
     */
    private int riderNum;

    public Vehicle() {}

    public Vehicle(int did, String maker, String model, int year, int riderNum) {
        this.did = did;
        this.maker = maker;
        this.model = model;
        this.year = year;
        this.riderNum = riderNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRiderNum() {
        return riderNum;
    }

    public void setRiderNum(int riderNum) {
        this.riderNum = riderNum;
    }
}
