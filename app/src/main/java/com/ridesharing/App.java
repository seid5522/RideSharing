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
package com.ridesharing;

import android.app.Application;

import java.util.Arrays;
import java.util.List;
import org.acra.*;
import org.acra.annotation.*;

import dagger.ObjectGraph;

/**
 * @Package com.ridesharing
 * @Author wensheng
 * @Date 2014/10/25.
 */
@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        formUri = "http://maxwell.sju.edu/~wy590204/rest/report.php"
)
public class App extends Application {
    private ObjectGraph applicationGraph;
    public static String BaseURL;
    @Override
    public void onCreate(){
        super.onCreate();
        //Intent mServiceIntent = new Intent(getApplicationContext(), LocationServiceImpl.class);
        //getApplicationContext().startService(mServiceIntent);
        //LocationServiceImpl_.intent(this).start();
        applicationGraph = ObjectGraph.create(getModules().toArray());
        BaseURL = getString(R.string.serverURL);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        //LocationServiceImpl_.intent(this).stop();
       // LocationServiceImpl.intent(getApplicationContext()).stop();
        //Intent mServiceIntent = new Intent(getApplicationContext(), LocationServiceImpl.class);
        //getApplicationContext().stopService(mServiceIntent);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AppModule(this));
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}
