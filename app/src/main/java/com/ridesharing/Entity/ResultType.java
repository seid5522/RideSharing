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

import com.google.gson.annotations.SerializedName;

public enum ResultType {
	@SerializedName("0")
	Success(0), 
	
	@SerializedName("1")
	Error(1),

    @SerializedName("2")
    RequireAuthenticate(2);
	
	private int value;
	private ResultType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
