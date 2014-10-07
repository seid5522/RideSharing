package com.ridesharing.Service;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;

/**
 * Created by wensheng on 2014/10/5.
 */
public class LocationService {
    public static Location getCurrentLocation(Activity activity){
        // Get the location manager
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        return locationManager.getLastKnownLocation(provider);
    }
}
