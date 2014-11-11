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
package com.ridesharing.Service;

import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.Vehicle;
import com.ridesharing.network.VehicleServiceClient;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class VehicleServiceImpl implements VehicleService {
    Vehicle vehicle;
    @Override
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    @Override
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public Result add(Vehicle vehicle) {
        return VehicleServiceClient.add(vehicle);
    }

    @Override
    public Result remove(Vehicle vehicle) {
        return VehicleServiceClient.remove(vehicle);
    }

    @Override
    public Result update(Vehicle vehicle) {
        return VehicleServiceClient.update(vehicle);
    }
}
