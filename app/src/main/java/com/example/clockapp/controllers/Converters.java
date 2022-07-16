package com.example.clockapp.controllers;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class Converters {
    @TypeConverter
    public static Calendar fromTimestamp(Long value) {
        Calendar cal=Calendar.getInstance();
        if(value==null)
            return null;
        cal.setTimeInMillis(value);
        return cal ;
    }

    @TypeConverter
    public static Long CalnderToTimestamp(Calendar date) {
        return date == null ? null : date.getTimeInMillis();
    }
}