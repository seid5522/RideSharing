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

public class Result {
	private ResultType type;
	private String message;
    private String data;

    public Result(){}

    public Result(ResultType type){
        this.type = type;
    }
	
	public Result(ResultType type, String message) {
		this.type = type;
		this.message = message;
	}

    public Result(ResultType type, String message, String data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
