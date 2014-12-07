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
import com.ridesharing.Entity.Request;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultData;

import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/12/7.
 */
public class RequestClient {
    private static final String BASE_URL = App.BaseURL + "RequestService.php";

    public static ResultData<ArrayList<Request>> fetchAll(){
        String url = BASE_URL + "?type=get";
        RestHelper<String, ResultData<ArrayList<Request>>> helper = new RestHelper<>(url, "", new ParameterizedTypeReference<ResultData<ArrayList<Request>>>() {});
        return helper.execute(true);
    }

    private static Result base(String type, Request request){
        String url = BASE_URL + "?type=" + type;
        RestHelper<Request, Result> helper = new RestHelper<>(url, request, new ParameterizedTypeReference<Result>() {});
        return helper.execute(false);
    }

    public static Result add(Request request){
        return base("add", request);
    }

    public static Result remove(Request request){
        return base("remove", request);
    }

    public static Result update(Request request){return  base("update", request);}
}
