package com.example.clockapp.controllers;
import android.app.Activity;

import com.example.clockapp.Addalarm;
import com.example.clockapp.MainActivity;
import com.example.clockapp.modules.Alarm;

import org.chromium.base.task.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;

public class DB_Asenctask extends AsyncTask<ArrayList<Alarm>>{
    WeakReference<Activity> mainActivityWeakReference;
    Alarm alarm;
    int task ;// 0 get all ,1 update ,2 insert new,3 delete

    public DB_Asenctask(Activity activity, Alarm alarm,int task) {
        this.mainActivityWeakReference = new WeakReference<Activity>(activity);
        this.alarm = alarm;
        this.task=task;
    }

    @Override
    protected ArrayList<Alarm> doInBackground() {
        ArrayList<Alarm>alarms = new ArrayList<Alarm>();
        RoomDB db=RoomDB.getInstance(mainActivityWeakReference.get());

        if(task==0) {
           alarms=new ArrayList<Alarm>(db.maindao().get_all_alarms()) ;
            alarms.sort((Comparator<Alarm>) (alarm, t1) -> Alarm.compare(alarm, t1));
        }
        else if (task==1)
        {
            db.maindao().update(alarm.getId(), alarm.getTime(),alarm.isActive(), alarm.getRepeat());
        }
        else if(task==2)
            {
               long id= db.maindao().insert(alarm);
               alarm.setId((int)id);
            }
        else {
             db.maindao().delete(alarm);
        }
        return  alarms;
    }

    @Override
    protected void onPostExecute(ArrayList<Alarm> alarms) {
                      if (task==0) {
                          MainActivity main=(MainActivity) mainActivityWeakReference.get();
                          main.inailzedb_getalarms(alarms);
                      }
                      else if(task==2){
                          Addalarm addalarm=(Addalarm)  mainActivityWeakReference.get();
                          addalarm.doneloading();
                      }
    }
}
