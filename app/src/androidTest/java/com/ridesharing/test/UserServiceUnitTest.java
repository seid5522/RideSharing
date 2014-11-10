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
package com.ridesharing.test;

import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultType;
import com.ridesharing.Entity.User;
import com.ridesharing.Service.AuthenticationService;
import com.ridesharing.Service.AuthenticationServiceImpl;
import com.ridesharing.Service.UserService;
import com.ridesharing.Service.UserServiceImpl;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/10/24.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class UserServiceUnitTest {
    AuthenticationServiceImpl service;
    @Before
    public void initial(){
        service = new AuthenticationServiceImpl();
    }

    @Test
    public void testLogin() throws Exception{
        User user = new User("yanwsh@gmail.com","888888");
        Result res = service.Login(user);
        assertTrue(res.getType() == ResultType.Success);
    }

    @Test
    public void testAuthorized() throws Exception{
        User user = new User("yanwsh@gmail.com","888888");
        Result res = service.Login(user);
        assertTrue(res.getType() == ResultType.Success);
        assertTrue(service.isAuthorized(user));
    }

    @Test
    public void testLoginFailed() throws Exception{
        User user = new User("yanwsh@gmail.com","123456");
        Result res = service.Login(user);
        assertTrue(res.getType() == ResultType.Error);
    }
}
