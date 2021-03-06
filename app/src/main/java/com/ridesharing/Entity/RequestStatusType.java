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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

/**
 * @Package com.ridesharing.Entity
 * @Author wensheng
 * @Date 2014/12/6.
 */
public enum RequestStatusType {
    @SerializedName("0")
    @JsonProperty("0")
    STATUS_NOT_CONFIRM(0),

    @JsonProperty("1")
    @SerializedName("1")
    STATUS_AGREE(1),

    @JsonProperty("2")
    @SerializedName("2")
    STATUS_DISAGREE(2);

    private int value;

    private RequestStatusType(int value){
        this.value = value;
    }

    @JsonValue
    public int getValue(){
        return value;
    }

    @JsonCreator
    public static RequestStatusType fromValue(String status) {
        if(status != null) {
            for(RequestStatusType featureStatus : RequestStatusType.values()) {
                Integer statusInt = Integer.parseInt(status);
                if(featureStatus.getValue() == statusInt) {
                    return featureStatus;
                }
            }

            throw new IllegalArgumentException(status + " is an invalid value.");
        }

        throw new IllegalArgumentException("A value was not provided.");
    }
}
