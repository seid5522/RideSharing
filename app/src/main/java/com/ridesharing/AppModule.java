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

import com.ridesharing.Service.ServiceModule;
import com.ridesharing.ui.login.LoginActivity_;
import com.ridesharing.ui.main.DefaultFragment_;
import com.ridesharing.ui.main.MainActivity_;
import com.ridesharing.ui.main.NavigationDrawerFragment;
import com.ridesharing.ui.main.NavigationDrawerFragment_;
import com.ridesharing.ui.user.registerActivity_;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @Package com.ridesharing
 * @Author wensheng
 * @Date 2014/10/25.
 */

@Module(
        injects = {
                MainActivity_.class,
                LoginActivity_.class,
                registerActivity_.class,
                DefaultFragment_.class,
                NavigationDrawerFragment_.class
        },
        includes = {
                ServiceModule.class
        }
)
public class AppModule {

    private App app;

    public AppModule(App app){this.app = app;}

   @Provides @Singleton public Application provideApplication(){return app;}
}
