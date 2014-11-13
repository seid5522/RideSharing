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
import android.app.Application;

import com.ridesharing.App;
import com.ridesharing.AppModule;
import com.ridesharing.ui.login.LoginActivity;
import com.ridesharing.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/10/25.
 */
@Module(
    complete = false,
    library = true
)
public class ServiceModule {


    @Provides @Singleton public AuthenticationService provideAuthenticationService(){
        return new AuthenticationServiceImpl();
    }

    @Provides @Singleton public DriverService provideDriverService(){
        return new DriverServiceImpl();
    }

    @Provides @Singleton public SearchPlaceService provideSearchPlaceService(Application app){
        return new SearchPlaceServiceImpl(app);
    }

    @Provides @Singleton public UserService provideUserService(){
        return new UserServiceImpl();
    }

    @Provides @Singleton public VehicleService provideVehicleService(){
        return new VehicleServiceImpl();
    }

    @Provides @Singleton public WishService provideWishService(){
        return new WishServiceImpl();
    }


}
