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

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.ridesharing.Entity.User;
import com.ridesharing.Entity.Wish;
import com.ridesharing.Entity.WishType;
import com.ridesharing.R;
import com.ridesharing.Service.LocationServiceImpl;
import com.ridesharing.Service.UserService;
import com.ridesharing.Service.WishService;

/**
 * @Package com.ridesharing.ui.main
 * @Author wensheng
 * @Date 2014/11/21.
 */
public class MarkerWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private WishService wishService;
    private UserService userService;
    private DefaultFragment fragment;
    private View mWindow;

    public MarkerWindowAdapter(WishService wishService, UserService userService, DefaultFragment fragment) {
        this.wishService = wishService;
        this.userService = userService;
        this.fragment = fragment;

        LayoutInflater LayoutInflater =
                (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindow = LayoutInflater.inflate(R.layout.marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if(marker.getTitle().equals(fragment.getText(R.string.currentLocation))){
            return null;
        }
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view) {
        String title = marker.getTitle();
        Wish wish = wishService.getWishHashtable().get(title);
        if(wish != null){
            TextView username = (TextView)view.findViewById(R.id.marker_username);
            TextView type = (TextView)view.findViewById(R.id.marker_type);
            TextView toadd1 = (TextView)view.findViewById(R.id.marker_toAddr1);
            TextView toadd2 = (TextView)view.findViewById(R.id.marker_toAddr2);
            User user = userService.getUserTables().get(wish.getUid());
            String displayName = user.getFirstname() + " " + user.getLastname();
            username.setText(displayName);
            String strType = fragment.getText(R.string.request).toString();
            if(wish.getType() == WishType.Offer){
                strType = fragment.getText(R.string.offer).toString();
            }
            type.setText(strType);
            String toAddr = wish.getToAddr();
            String toAddr1 = "";
            if(toAddr == null || toAddr.equals("")){
                Address address = LocationServiceImpl.getLocationFromLatLng(fragment.getActivity(), wish.getTolat(), wish.getTolng());
                toAddr = address.getAddressLine(0) + ", ";
                toAddr1 = address.getAddressLine(1);
            }
            toadd1.setText(toAddr);
            toadd2.setText(toAddr1);
        }
    }
}
