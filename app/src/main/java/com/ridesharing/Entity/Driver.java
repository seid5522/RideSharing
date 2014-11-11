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
public class Driver {
    private int id;
    private int uid;
    private String drlicense;

    public Driver(int uid, String drlicense) {
        this.uid = uid;
        this.drlicense = drlicense;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrlicense() {
        return drlicense;
    }

    public void setDrlicense(String drlicense) {
        this.drlicense = drlicense;
    }
}
