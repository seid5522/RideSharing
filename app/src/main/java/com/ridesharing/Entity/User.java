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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ridesharing.Json.CustomJsonDateDeserializer;
import com.ridesharing.Json.CustomJsonDateSerializer;
import com.ridesharing.crypto.MD5;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
	private int id;
	private String username;
	private String password;
    private String email;
	private String firstname;
	private String lastname;
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date birthday;
	private String address;
    private String address2;
    private String city;
    private String state;
    private String zipcode;
    private String phone;
    private String photoURL;
    private String sessionKey;
    private String deviceId;
    MD5 md5 = new MD5();

    //dummy constructor for json de-serialize
    public User() {}

    public User(int id) {
        this.id = id;
    }

    public User(String username, String password) {
		this.username = username;
		this.password = md5.EncryptToString(password);
	}

    public User(String username, String email, String firstname, String lastname, Date birthday, String address, String address2, String city, String state, String zipcode, String phone, String photoURL, String sessionKey, String deviceId) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phone = phone;
        this.photoURL = photoURL;
        this.sessionKey = md5.EncryptToString(sessionKey);
        this.deviceId = deviceId;
    }

    public User( String username, String password, String email, String firstname, String lastname, Date birthday, String address, String address2, String city, String state, String zipcode, String phone, String photoURL, String deviceId) {
        this.username = username;
        MD5 md5 = new MD5();
        this.password = md5.EncryptToString(password);
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phone = phone;
        this.photoURL = photoURL;
        this.deviceId = deviceId;
    }

    public User(User user){
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.birthday = user.birthday;
        this.address = user.address;
        this.address2 = user.address2;
        this.city = user.city;
        this.state = user.state;
        this.zipcode = user.zipcode;
        this.phone = user.phone;
        this.photoURL = user.photoURL;
        this.deviceId = user.deviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = md5.EncryptToString(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = md5.EncryptToString(sessionKey);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
