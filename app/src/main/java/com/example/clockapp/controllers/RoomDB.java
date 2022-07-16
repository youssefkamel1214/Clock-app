package com.example.clockapp.controllers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.clockapp.modules.Alarm;

@Database( entities = {Alarm.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {
    private   static  RoomDB db;
    private static  String dbname="database";
    public synchronized static  RoomDB getInstance(Context context){
        if(db==null)
        {
            db= Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,dbname).
                    allowMainThreadQueries().
                    fallbackToDestructiveMigration().
                    build();
        }
        return  db;
    }
    public abstract  MAindao maindao();
}
