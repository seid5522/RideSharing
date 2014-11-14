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
import android.graphics.Point;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesharing.App;
import com.ridesharing.Entity.ELocation;
import com.ridesharing.R;
import com.ridesharing.Service.LocationService;
import com.ridesharing.Service.LocationServiceImpl_;

import java.lang.annotation.ElementType;

import dagger.ObjectGraph;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * @Package com.ridesharing.ui.main
 * @Author wensheng
 * @Date 2014/11/11.
 */
public class CustomGoogleMap extends com.google.android.gms.maps.SupportMapFragment{
    DefaultFragment mainMap;
    public CustomGoogleMap(){super();}

    public static CustomGoogleMap newInstance(DefaultFragment mainMap){
        CustomGoogleMap frag = new CustomGoogleMap();
        frag.mainMap = mainMap;
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainMap.addMarkerToMap();
    }
}
