package com.example.clockapp.controllers;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.clockapp.modules.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Dao
public interface MAindao {
    @Insert(onConflict = REPLACE)
    Long  insert(Alarm alarm);
    @Delete
    void  delete(Alarm alarm);
    @Query("Update Alarms_table set repeat = :srepeat, active= :sactive ,time= :stime where id = :sid")
    void update(int sid, Calendar stime,boolean sactive, String srepeat);
    @Query("Select * from Alarms_table ORDER BY time Desc")
    List<Alarm> get_all_alarms();
}
