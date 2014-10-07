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
package com.ridesharing.network;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.User;

import android.R.string;
import android.util.Log;

/**
 * UserServiceHelper class connect server directly by using SOAP protocol.
 * @author wensheng
 *
 */
public class UserServiceHelper {
    private static final String LOGIN_METHOD_NAME = "login";
    private static final String LOGIN_URL = "UserService.php?wsdl";
    
    /**
     *  Login Function
     *  How to use:	
	 *  User user = new User("yanwsh", "888888");
     *  UserServiceHelper.Login(user);
     * @param user User class only contain username and password function
     * @return Result Type, use result.type to get detail. 
     */
	public static Result Login(User user){
		SOAPHelper helper = new SOAPHelper(LOGIN_URL, LOGIN_METHOD_NAME);
		Gson gson = Manager.getGson();
		helper.addProperty("user" , gson.toJson(user));
		String msg = helper.run();
		return gson.fromJson(msg, Result.class);
	}
}
