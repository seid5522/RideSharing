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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @Package com.ridesharing.Entity
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class Driver extends User {
    @JsonProperty("did")
    private int did;

    private int uid;

    private Vehicle vehicle;

    public Driver(){}

    public Driver(String username, String password, String email, String firstname, String lastname, Date birthday, String address, String address2, String city, String state, String zipcode, String phone, String photoURL, int uid, Vehicle vehicle) {
        super(username, password, email, firstname, lastname, birthday, address, address2, city, state, zipcode, phone, photoURL);
        this.uid = uid;
        this.vehicle = vehicle;
    }

    public Driver(User user, int uid, Vehicle vehicle){
        super(user);
        this.uid = uid;
        this.vehicle = vehicle;
    }

    public Driver(int Did) {
        this.did = Did;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
