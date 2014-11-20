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
import com.ridesharing.Entity.Wish;

import java.util.ArrayList;

/**
 * @Package com.ridesharing.Service
 * @Author wensheng
 * @Date 2014/11/11.
 */
public interface WishService extends EntityService<Wish> {
    public ArrayList<Wish> search(Wish wish);
    public ArrayList<Wish> fetchAll(Wish wish, int distance);
}
