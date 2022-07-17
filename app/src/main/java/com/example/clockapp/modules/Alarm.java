package com.example.clockapp.modules;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Comparator;
import java.util.PrimitiveIterator;
@Entity(tableName = "Alarms_table")
public class Alarm implements Serializable {
    private String Title;
    private Calendar time;
    private String repeat;
    private int tone;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private  boolean active;



    public int getTone() {
        return tone;
    }

    public void setTone(int tone) {
        this.tone = tone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
            this.time = time;
            this.time.set(Calendar.SECOND, 0);
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
        Calendar now=Calendar.getInstance();
      if(repeat.indexOf("1")==-1)
          return;
      time.set(Calendar.DATE, now.get(Calendar.DATE));
      while (now.after(time)||repeat.charAt(time.get(Calendar.DAY_OF_WEEK)-1)=='0')
      {
          time.add(Calendar.DAY_OF_WEEK, 1);
      }
    }
    public static int compare(Alarm a1, Alarm a2) {
            if(a1.isActive()^a2.isActive())
                return  a1.isActive()?-1:1;
            else
            {
                if(Calendar.getInstance().after(a1.getTime()))
                    return  1;
                else if (Calendar.getInstance().after(a2.getTime()))
                    return -1;
                if(a1.getTime().before(a2.getTime()))
                    return -1;
                else
                    return 1;

            }

        }

}
