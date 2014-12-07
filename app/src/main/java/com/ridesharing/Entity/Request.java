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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ridesharing.Json.CustomJsonDateDeserializer;
import com.ridesharing.Json.CustomJsonDateSerializer;

import java.util.Date;

/**
 * @Package com.ridesharing.Entity
 * @Author wensheng
 * @Date 2014/12/6.
 */
public class Request {
    private int id;
    private int uid;
    private int askuid;
    private int wid;
    private RoleType role;
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date createTime;
    private RequestStatusType status;

    public Request() {
    }

    public Request(int uid, int askuid, int wid, RoleType role, Date createTime, RequestStatusType status) {
        this.uid = uid;
        this.askuid = askuid;
        this.wid = wid;
        this.role = role;
        this.createTime = createTime;
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

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public RequestStatusType getStatus() {
        return status;
    }

    public void setStatus(RequestStatusType status) {
        this.status = status;
    }

    public int getAskuid() {
        return askuid;
    }

    public void setAskuid(int askuid) {
        this.askuid = askuid;
    }
}
