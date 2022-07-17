package com.example.clockapp.controllers;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

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
             cancelAlarm(alarm,mainActivityWeakReference.get());
        }
        if(task==1||task==2)
        {
            setAlarm(alarm, mainActivityWeakReference.get());
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
    private void setAlarm(Alarm alarm,Context context){
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReciver.class);
        intent.putExtra("id",alarm.getId());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,alarm.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarm.getTime().getTimeInMillis(),pendingIntent);

    }
    private void cancelAlarm(Alarm alarm,Context context) {
        Intent intent=new Intent(context, AlarmReciver.class);
        intent.putExtra("id",alarm.getId());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context
                ,alarm.getId(),intent,PendingIntent.FLAG_NO_CREATE);
        try {

        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        }catch (Exception exception){
            Toast.makeText(context,exception.getMessage(), Toast.LENGTH_LONG);
        }
    }

}
