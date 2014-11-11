package com.ridesharing.Service;

import com.ridesharing.Entity.Result;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/11/11.
 */
public interface EntityService<T> {
    public Result add(T t);
    public Result remove(T t);
    public Result update(T t);
}
