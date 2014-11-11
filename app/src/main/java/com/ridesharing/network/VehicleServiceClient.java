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
import com.ridesharing.Entity.Vehicle;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class VehicleServiceClient {
    private static final String BASE_URL = App.BaseURL + "VehicleService.php";

    private static Result base(Vehicle vehicle, String type){
        String url = BASE_URL + "?type=" + type;
        RestHelper<Vehicle, Result> helper = new RestHelper<>(url, vehicle, Result.class);
        return helper.execute();
    }

    public static Result add(Vehicle vehicle){
        return base(vehicle, "add");
    }

    public static Result update(Vehicle vehicle){
        return base(vehicle, "update");
    }

    public static Result remove(Vehicle vehicle){
        return base(vehicle, "remove");
    }
}
