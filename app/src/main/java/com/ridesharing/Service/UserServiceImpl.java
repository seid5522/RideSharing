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

import com.google.android.gms.common.api.GoogleApiClient;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.User;
import com.ridesharing.network.UserServiceClient;

import java.util.Hashtable;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/10/25.
 */
public class UserServiceImpl implements UserService {
    private User user;
    private boolean isDriver;
    private Hashtable<Integer, User> userHashtable;

    public UserServiceImpl(){
        isDriver = false;
        userHashtable = new Hashtable<>();
    }

    public Result Register(User user){
        return  UserServiceClient.register(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public User fetchSelfInfo(){
        return UserServiceClient.fetchSelfInfo().getData();
    }
    public User fetchOtherInfo(int id){
        User user = new User(id);
        return UserServiceClient.fetchOtherInfo(user).getData();
    }

    @Override
    public boolean isDriver() {
        return isDriver;
    }

    @Override
    public void setDriver(boolean isDriver) {
        this.isDriver = isDriver;
    }

    @Override
    public Boolean isRegister(String email) {
        return UserServiceClient.isRegister(email).getData();
    }

    @Override
    public Hashtable<Integer, User> getUserTables(){
        return userHashtable;
    }

    public void setUserHashtable(Hashtable<Integer, User> userHashtable) {
        this.userHashtable = userHashtable;
    }
}
