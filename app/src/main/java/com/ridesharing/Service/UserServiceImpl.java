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

import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.User;
import com.ridesharing.network.UserServiceClient;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/10/25.
 */
public class UserServiceImpl implements UserService {
    private User user;
    public UserServiceImpl(){

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
        return null;
    }
    public User fetchOtherInfo(int id){
        return null;
    }
}
