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
package com.ridesharing.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.ridesharing.App;
import com.ridesharing.R;
import com.ridesharing.Service.LocationService;
import com.ridesharing.Service.LocationServiceImpl_;

import dagger.ObjectGraph;

/**
 * @Package com.ridesharing.ui.main
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class CustomGoogleMap extends com.google.android.gms.maps.SupportMapFragment{
    DefaultFragment fragment;
    public CustomGoogleMap(){super();}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GoogleMap map = this.getMap();
        if(fragment == null){
            Log.e("com.ridesharing.CustomGoogleMap:", "fragment is null.");
            return;
        }
        fragment.map = map;
        if (map != null) {
            if(fragment.locationService.getLastBestLocation() != null){
                fragment.location = fragment.locationService.getLastBestLocation();
            }else if(fragment.locationService.getLastLocation() != null){
                fragment.location = fragment.locationService.getLastLocation();
            }else{
                fragment.location = fragment.locationService.getLastKnowLocation();
            }
            if(fragment.location != null){
                fragment.addMapMarker("Current Location", new LatLng(fragment.location.getLatitude(), fragment.location.getLongitude()));
                fragment.stopLocationService();
                Address addr = LocationServiceImpl_.getLocationFromAddress(getActivity(), "5600 City Ave, Philadelphia, PA");
                fragment.addMapMarker("School", new LatLng(addr.getLatitude(), addr.getLongitude()));
            }
        }
    }

    public void setFragment(DefaultFragment fragment){
        this.fragment = fragment;
    }
}
