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
package com.ridesharing.network;

import com.ridesharing.App;
import com.ridesharing.Entity.Driver;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultData;
import com.ridesharing.Entity.ResultType;

import org.springframework.core.ParameterizedTypeReference;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class DriverServiceClient {
    private static final String BASE_URL = App.BaseURL + "DriverService.php";

    private static Result base(Driver driver, String type){
        String url = BASE_URL + "?type=" + type;
        RestHelper<Driver, Result> helper = new RestHelper<>(url, driver, new ParameterizedTypeReference<Result>() {});
        return helper.execute(false);
    }

    public static Result add(Driver driver){
        return base(driver, "register");
    }

    public static Result update(Driver driver){
        return new Result(ResultType.Error, "Function not support.");
    }

    public static Result remove(Driver driver){
        return base(driver, "remove");
    }

    public static ResultData<Boolean> isDriver(){
        String url = BASE_URL + "?type=isDriver";
        ResultData<Boolean> result = new ResultData<>();
        RestHelper<String, ResultData<Boolean>> helper = new RestHelper<>(url, "", new ParameterizedTypeReference<ResultData<Boolean>>() {});
        return helper.execute(false);
    }

    public static ResultData<Driver> fetchSelfInfo(){
        String url = BASE_URL + "?type=info";
        ResultData<Driver> result = new ResultData<>();
        RestHelper<String, ResultData<Driver>> helper = new RestHelper<>(url, "", new ParameterizedTypeReference<ResultData<Driver>>() {});
        return helper.execute(true);
    }
}
