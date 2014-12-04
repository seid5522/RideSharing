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
package com.ridesharing.Utility;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * @Package com.ridesharing.Utility
 * @Author wensheng
 * @Date 2014/12/3.
 */
public class DataProcess {
    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year =  datePicker.getYear();

        int month2 = Calendar.JANUARY;


        switch(month){
            case 1:
                month2 = Calendar.JANUARY;
                break;
            case 2:
                month2 = Calendar.FEBRUARY;
                break;
            case 3:
                month2 = Calendar.MARCH;
                break;
            case 4:
                month2 = Calendar.APRIL;
                break;
            case 5:
                month2 = Calendar.MAY;
                break;
            case 6:
                month2 = Calendar.JUNE;
                break;
            case 7:
                month2 = Calendar.JULY;
                break;
            case 8:
                month2 = Calendar.AUGUST;
                break;
            case 9:
                month2 = Calendar.SEPTEMBER;
                break;
            case 10:
                month2 = Calendar.OCTOBER;
                break;
            case 11:
                month2 = Calendar.NOVEMBER;
                break;
            case 12:
                month2 = Calendar.DECEMBER;
                break;
        }


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month2, day);

        return calendar.getTime();
    }

    public static Date getDateFromDatePickerAndTimePicer(DatePicker datePicker, TimePicker timePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year =  datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        int month2 = Calendar.JANUARY;


        switch(month){
            case 1:
                month2 = Calendar.JANUARY;
                break;
            case 2:
                month2 = Calendar.FEBRUARY;
                break;
            case 3:
                month2 = Calendar.MARCH;
                break;
            case 4:
                month2 = Calendar.APRIL;
                break;
            case 5:
                month2 = Calendar.MAY;
                break;
            case 6:
                month2 = Calendar.JUNE;
                break;
            case 7:
                month2 = Calendar.JULY;
                break;
            case 8:
                month2 = Calendar.AUGUST;
                break;
            case 9:
                month2 = Calendar.SEPTEMBER;
                break;
            case 10:
                month2 = Calendar.OCTOBER;
                break;
            case 11:
                month2 = Calendar.NOVEMBER;
                break;
            case 12:
                month2 = Calendar.DECEMBER;
                break;
        }


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month2, day, hour, minute);
        return calendar.getTime();
    }
}
