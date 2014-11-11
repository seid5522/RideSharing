package com.ridesharing.Service;

import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.Vehicle;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/11/11.
 */
public interface VehicleService extends EntityService<Vehicle> {
    public Vehicle getVehicle();
    public void setVehicle(Vehicle vehicle);
}
