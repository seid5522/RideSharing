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
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultData;
import com.ridesharing.Entity.User;

import org.springframework.core.ParameterizedTypeReference;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/11/1.
 */
public class UserServiceClient {
    private static final String BASE_URL = App.BaseURL + "UserService.php";

    private static Result base(User user, String type){
        String url = BASE_URL + "?type=" + type;
        RestHelper<User, Result> helper = new RestHelper<>(url, user, new ParameterizedTypeReference<Result>() {});
        return helper.execute(false);
    }

    public static ResultData<Boolean> isRegister(String email){
        String url = BASE_URL + "?type=isregister&email=" + email;
        RestHelper<String, ResultData<Boolean>> helper = new RestHelper<>(url, "", new ParameterizedTypeReference<ResultData<Boolean>>() {});
        return helper.execute(false);
    }

    public static Result register(User user){
        return base(user, "register");
    }

    public static Result getUserProfile(User user){
        return base(user, "getProfile");
    }

    public static Result update(User user){
        return base(user, "update");
    }

    public static ResultData<User> fetchSelfInfo(){
        String url = BASE_URL + "?type=info";
        ResultData<User> result = new ResultData<>();
        RestHelper<String, ResultData<User>> helper = new RestHelper<>(url, "", new ParameterizedTypeReference<ResultData<User>>() {});
        return helper.execute(true);
    }

    public static ResultData<User> fetchOtherInfo(User user){
        String url = BASE_URL + "?type=info";
        ResultData<User> result = new ResultData<>();
        RestHelper<User, ResultData<User>> helper = new RestHelper<>(url, user, new ParameterizedTypeReference<ResultData<User>>() {});
        return helper.execute(true);
    }

    public static Result updateDevice(User user){
        return base(user, "updateDevice");
    }
}
