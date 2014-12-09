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

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;

import java.io.IOException;
import java.util.List;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/10/25.
 */
@EService
public class LocationServiceImpl extends Service implements LocationService {
    private static final String TAG = "com.ridesharing.Service.LocationServiceImpl";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private IBinder mBinder;
    protected Location lastLocation;
    protected Location lastBestLocation;
    @SystemService
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected LocationFinishedListener locationFinishedListener;

    public LocationServiceImpl(){
        mBinder = new LocationServiceBinder();
    }

    /**
     * Class used for the client Binder.
     */
    public class LocationServiceBinder extends Binder {
        public LocationService getService(){
            return LocationServiceImpl.this;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.v(TAG, "LocationService:onCreate");
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastLocation = location;
                Location fixLocation = getLastKnowLocation();
                if(isBetterLocation(location, fixLocation)){
                    lastBestLocation = location;
                }
                if(locationFinishedListener != null && (getLastLocation() != null || getLastBestLocation() != null || getLastKnowLocation() != null )){
                    locationFinishedListener.onFinished();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v(TAG, "LocationService:onDestroy");
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return mBinder;
    }


    public Location getLastLocation() {
        return lastLocation;
    }

    public Location getLastBestLocation() {
        return lastBestLocation;
    }

    public Location getLastKnowLocation(){
        return locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static Address getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        Address p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            p1 = address.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    public static Address getLocationFromLatLng(Context context, double latitude, double longitude){
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        Address p1 = null;
        try {
            address = coder.getFromLocation(latitude, longitude, 1);
            if (address == null) {
                return null;
            }
            p1 = address.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  p1;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void setLocationFinishedListener(LocationFinishedListener locationFinishedListener) {
        this.locationFinishedListener = locationFinishedListener;
    }
}
