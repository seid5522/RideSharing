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

import java.util.Hashtable;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/10/23.
 */
public interface UserService {
    public Result Register(User user);
    public User getUser();
    public Hashtable<Integer, User> getUserTables();
    public void setUser(User user);
    public User fetchSelfInfo();
    public User fetchOtherInfo(int id);
    public boolean isDriver();
    public void setDriver(boolean isDriver);
    public Boolean isRegister(String email);
}
