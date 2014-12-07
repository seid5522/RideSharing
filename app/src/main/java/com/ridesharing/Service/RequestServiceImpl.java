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

import com.ridesharing.Entity.Request;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultData;
import com.ridesharing.network.RequestClient;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/12/7.
 */
public class RequestServiceImpl implements RequestService {
    private Hashtable<String, Request> requestHashtable;
    @Override
    public Hashtable<String, Request> getRequestHashtable() {
        return requestHashtable;
    }

    @Override
    public ArrayList<Request> fetchAll() {
        ResultData<ArrayList<Request>> resultData = RequestClient.fetchAll();
        return resultData.getData();
    }

    @Override
    public Result add(Request request) {
        return RequestClient.add(request);
    }

    @Override
    public Result remove(Request request) {
        return RequestClient.remove(request);
    }

    @Override
    public Result update(Request request) {
        return RequestClient.update(request);
    }
}
