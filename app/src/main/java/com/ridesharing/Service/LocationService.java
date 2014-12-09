package com.ridesharing.Service;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.os.Bundle;

/**
 * Created by wensheng on 2014/10/5.
 */
public interface LocationService {
    public Location getLastLocation();
    public Location getLastBestLocation();
    public Location getLastKnowLocation();
    public void setLocationFinishedListener(LocationFinishedListener locationFinishedListener);
}
