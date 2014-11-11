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

import android.content.res.Resources;

import com.ridesharing.App;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.User;
import com.ridesharing.R;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/11/3.
 */
public class AuthServiceClient {
    private static final String BASE_URL = App.BaseURL + "AuthService.php";

    private static Result base(User user, String type){
        String url = BASE_URL + "?type=" + type;
        RestHelper<User, Result> helper = new RestHelper<>(url, user, Result.class);
        return helper.execute();
    }

    public static Result login(User user){
        return base(user, "login");
    }

    public static Result isAuthorized(User user){
        return base(user, "isAuth");
    }

    public static Result logout(User user){
        return base(user, "logout");
    }
}
